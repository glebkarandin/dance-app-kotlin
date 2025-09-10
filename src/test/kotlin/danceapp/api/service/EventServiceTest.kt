package danceapp.api.service

import danceapp.api.model.Event
import danceapp.api.repository.EventRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@ExtendWith(MockitoExtension::class)
class EventServiceTest {

    @Mock
    private lateinit var eventRepository: EventRepository

    @InjectMocks
    private lateinit var eventService: EventService

    @Test
    fun `getEventById should return event when event exists`() {
        // Arrange
        val eventId = 1
        val mockEvent = Event().apply {
            id = eventId
            title = "Test Event"
            dateStart = OffsetDateTime.now(ZoneOffset.UTC)
            dateEnd = OffsetDateTime.now(ZoneOffset.UTC).plusHours(2)
        }

        `when`(eventRepository.findById(eventId)).thenReturn(Optional.of(mockEvent))

        // Act
        val foundEventOptional = eventService.getEventById(eventId)

        // Assert
        assertTrue(foundEventOptional.isPresent)
        val foundEvent = foundEventOptional.get()
        assertEquals(eventId, foundEvent.id)
        assertEquals("Test Event", foundEvent.title)
        verify(eventRepository, times(1)).findById(eventId)
    }

    @Test
    fun `getEventById should return empty when event does not exist`() {
        // Arrange
        val eventId = 1
        `when`(eventRepository.findById(eventId)).thenReturn(Optional.empty())

        // Act
        val foundEventOptional = eventService.getEventById(eventId)

        // Assert
        assertFalse(foundEventOptional.isPresent)
        verify(eventRepository, times(1)).findById(eventId)
    }

    @Test
    fun `createEvent should save and return event`() {
        // Arrange
        val eventToCreate = Event().apply {
            title = "New Event"
            dateStart = OffsetDateTime.now(ZoneOffset.UTC)
            dateEnd = OffsetDateTime.now(ZoneOffset.UTC).plusHours(3)
        }

        val savedEvent = Event().apply {
            id = 1
            title = eventToCreate.title
            dateStart = eventToCreate.dateStart
            dateEnd = eventToCreate.dateEnd
        }

        `when`(eventRepository.save(any(Event::class.java))).thenReturn(savedEvent)

        // Act
        val createdEvent = eventService.createEvent(eventToCreate)

        // Assert
        assertNotNull(createdEvent)
        assertEquals(1, createdEvent.id)
        assertEquals("New Event", createdEvent.title)
        verify(eventRepository, times(1)).save(eventToCreate)
    }
}
