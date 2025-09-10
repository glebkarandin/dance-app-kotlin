package danceapp.api.service

import danceapp.api.model.Event
import danceapp.api.repository.EventRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.Optional

@Service
@Transactional
class EventService(private val eventRepository: EventRepository) {

    fun getAllEvents(): List<Event> {
        return eventRepository.findAll()
    }

    fun getEventById(id: Int): Optional<Event> {
        return eventRepository.findById(id)
    }

    fun createEvent(event: Event): Event {
        // Add validation, e.g., dateStart before dateEnd
        return eventRepository.save(event)
    }

    fun updateEvent(id: Int, eventDetails: Event): Optional<Event> {
        return eventRepository.findById(id)
            .map { existingEvent ->
                existingEvent.title = eventDetails.title
                existingEvent.description = eventDetails.description
                existingEvent.dateStart = eventDetails.dateStart
                existingEvent.dateEnd = eventDetails.dateEnd
                existingEvent.city = eventDetails.city
                existingEvent.country = eventDetails.country
                eventRepository.save(existingEvent)
            }
    }

    fun deleteEvent(id: Int): Boolean {
        if (eventRepository.existsById(id)) {
            // Consider implications for Nominations linked to this event.
            eventRepository.deleteById(id)
            return true
        }
        return false
    }

    fun findEventsByDateRange(start: OffsetDateTime, end: OffsetDateTime): List<Event> {
        return eventRepository.findByDateStartBetween(start, end)
    }

    fun findEventsByLocation(city: String, country: String): List<Event> {
        return eventRepository.findByCityAndCountry(city, country)
    }
}
