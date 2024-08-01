package webCalendarSpring

import com.fasterxml.jackson.databind.JsonMappingException
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import java.time.LocalDate


@SpringBootApplication
@EnableWebMvc
class WebCalenderApplication


fun main(args: Array<String>) {
    runApplication<WebCalenderApplication>(*args)
}

@Validated
@RestController("")
class Controller(@Autowired val eventDao: EventDao) {


    // return all events for today's date
    @GetMapping("/event/today")
    fun today(): ResponseEntity<List<Event>> {
        val eventList = eventDao.findAll().filter { it.date == LocalDate.now() }
        return /*if (eventList.isEmpty()) {
            ResponseEntity.noContent().build()
        } else */ResponseEntity.ok(eventList)
    }

    // refactor for time ranges
    @GetMapping("/event")
    fun eventGet(
        @RequestParam(required = false, name = "start_time") start: LocalDate?,
        @RequestParam(required = false, name = "end_time") end: LocalDate?,
    ): ResponseEntity<List<Event>> {
        val eventList = if (start == null || end == null) {
            eventDao.findAll()
        } else {
            eventDao.findByDateBetween(start, end)
        }

        return if (eventList.isEmpty()) {
            ResponseEntity.noContent().build()
        } else ResponseEntity.ok(eventList)
    }

    @DeleteMapping("/event/{id}")
    fun deleteEventById(@PathVariable("id") id: Int): ResponseEntity<Event> {
        val event: Event = eventDao.findById(id).orElse(null) ?: throw EventNotFoundException()
        eventDao.delete(event)
        return ResponseEntity.ok(event)
    }

    // add msg DOESNT EXIST
    @GetMapping("/event/{id}")
    fun getEventById(@PathVariable("id") id: Int): ResponseEntity<Event> {
        val event: Event = eventDao.findById(id).orElse(null) ?: throw EventNotFoundException()
        return ResponseEntity(event, HttpStatus.OK)
    }

    @PostMapping("/event")
    fun eventPost(@Valid @RequestBody dto: EventRequestDTO): ResponseEntity<EventPostResponseDTO> {
        val eventity = Event(dto)
        eventDao.save(eventity)
        val response = EventPostResponseDTO("The event has been added!", eventity.event, eventity.date.toString())
        return ResponseEntity.ok(response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class, JsonMappingException::class)
    fun handleValidationException(): ResponseEntity<Void> {
        return ResponseEntity.badRequest().build()
    }

    @ExceptionHandler(EventNotFoundException::class,)
    fun handleEventNotFoundException(): ResponseEntity<Pair<String, String>> {
        return ResponseEntity("Error" to "The event doesn't exist!", HttpStatus.NOT_FOUND)
    }
}