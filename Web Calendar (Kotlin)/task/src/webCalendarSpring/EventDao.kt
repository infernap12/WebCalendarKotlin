package webCalendarSpring

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface EventDao : JpaRepository<Event, Int> {
    fun findByDateBetween(first: LocalDate, second: LocalDate): List<Event>
}

