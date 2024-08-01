package webCalendarSpring



import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import java.time.LocalDate


class EventRequestDTO @JsonCreator constructor(
    @JsonProperty("event") @field:NotBlank(message = "Event cannot be blank")
    var event: String,

    @JsonProperty("date") @field:NotNull(message = "Date cannot be null")
    var date: LocalDate
)

data class EventPostResponseDTO(
    val message: String,
    val event: String,
    val date: String
)