package webCalendarSpring

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

@Entity
@Table(name = "EVENT")
class Event(dto: EventRequestDTO) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID", nullable = false)
    var id: Int = 0

    @NotNull
    @Lob
    @Column(name = "EVENT_DESCRIPTION", nullable = false)
    var event: String = dto.event

    @NotNull
    @Column(name = "DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var date: LocalDate = dto.date
}