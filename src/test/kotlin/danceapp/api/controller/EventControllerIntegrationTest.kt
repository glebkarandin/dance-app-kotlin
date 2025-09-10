package danceapp.api.controller

import danceapp.api.model.Event
import danceapp.api.service.EventService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import org.mockito.BDDMockito.given
import org.hamcrest.Matchers.`is`

@WebMvcTest(EventController::class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class EventControllerIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockBean
    private lateinit var eventService: EventService

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @Test
    fun `getEventById should return EventDto when event exists`() {
        // Arrange
        val eventId = 1
        val mockEvent = Event().apply {
            id = eventId
            title = "Integration Test Event"
            description = "A test event for integration testing"
            dateStart = OffsetDateTime.of(2024, 8, 15, 10, 0, 0, 0, ZoneOffset.UTC)
            dateEnd = OffsetDateTime.of(2024, 8, 15, 12, 0, 0, 0, ZoneOffset.UTC)
            city = "Test City"
            country = "Test Country"
        }

        given(eventService.getEventById(eventId)).willReturn(Optional.of(mockEvent))

        // Act & Assert
        mockMvc.get("/bknd/api/events/{id}", eventId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id", `is`(eventId))
            jsonPath("$.title", `is`("Integration Test Event"))
            jsonPath("$.description", `is`("A test event for integration testing"))
            jsonPath("$.dateStart", `is`(mockEvent.dateStart.format(formatter)))
            jsonPath("$.dateEnd", `is`(mockEvent.dateEnd.format(formatter)))
            jsonPath("$.city", `is`("Test City"))
            jsonPath("$.country", `is`("Test Country"))
        }
    }

    @Test
    fun `getEventById should return not found when event does not exist`() {
        // Arrange
        val eventId = 99
        given(eventService.getEventById(eventId)).willReturn(Optional.empty())

        // Act & Assert
        mockMvc.get("/bknd/api/events/{id}", eventId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }
}
