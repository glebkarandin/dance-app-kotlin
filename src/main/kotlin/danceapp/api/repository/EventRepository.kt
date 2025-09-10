package danceapp.api.repository

import danceapp.api.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
interface EventRepository : JpaRepository<Event, Int> {
    fun findByDateStartBetween(start: OffsetDateTime, end: OffsetDateTime): List<Event>
    fun findByCityAndCountry(city: String, country: String): List<Event>
}
