package danceapp.api.controller

import danceapp.api.dto.EventDto
import danceapp.api.service.EventService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.OffsetDateTime

@RestController
@RequestMapping("/bknd/api/events")
class EventController(private val eventService: EventService) {

    @GetMapping
    fun getAllEvents(): List<EventDto?> {
        return eventService.getAllEvents().map(EventDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: Int): ResponseEntity<EventDto> {
        return eventService.getEventById(id)
            .map { event -> EventDto.fromEntity(event) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createEvent(@RequestBody dto: EventDto): ResponseEntity<EventDto> {
        val entity = dto.toEntity()
        val createdEntity = eventService.createEvent(entity)
        return ResponseEntity.status(HttpStatus.CREATED).body(EventDto.fromEntity(createdEntity))
    }

    @PutMapping("/{id}")
    fun updateEvent(@PathVariable id: Int, @RequestBody dto: EventDto): ResponseEntity<EventDto> {
        val entityDetails = dto.toEntity()
        return eventService.updateEvent(id, entityDetails)
            .map { event -> EventDto.fromEntity(event) }
            .map { updatedDto -> ResponseEntity.ok(updatedDto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteEvent(@PathVariable id: Int): ResponseEntity<Void> {
        return if (eventService.deleteEvent(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/search/findByDateRange")
    fun findEventsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: OffsetDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: OffsetDateTime
    ): List<EventDto?> {
        return eventService.findEventsByDateRange(start, end).map(EventDto::fromEntity)
    }

    @GetMapping("/search/findByLocation")
    fun findEventsByLocation(@RequestParam city: String, @RequestParam country: String): List<EventDto?> {
        return eventService.findEventsByLocation(city, country).map(EventDto::fromEntity)
    }
}
