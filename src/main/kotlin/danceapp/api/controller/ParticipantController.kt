package danceapp.api.controller

import danceapp.api.dto.ParticipantDto
import danceapp.api.dto.ParticipantResultDto
import danceapp.api.service.ParticipantService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bknd/api/participants")
class ParticipantController(private val participantService: ParticipantService) {

    @GetMapping
    fun getAllParticipants(): List<ParticipantDto?> {
        return participantService.getAllParticipants().map(ParticipantDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getParticipantById(@PathVariable id: Int): ResponseEntity<ParticipantDto> {
        return participantService.getParticipantById(id)
            .map { participant -> ParticipantDto.fromEntity(participant) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createParticipant(@RequestBody dto: ParticipantDto): ResponseEntity<ParticipantDto> {
        val entity = dto.toEntity()
        val createdEntity = participantService.createParticipant(entity)
        return ResponseEntity.status(HttpStatus.CREATED).body(ParticipantDto.fromEntity(createdEntity))
    }

    @PutMapping("/{id}")
    fun updateParticipant(@PathVariable id: Int, @RequestBody dto: ParticipantDto): ResponseEntity<ParticipantDto> {
        val entityDetails = dto.toEntity()
        return participantService.updateParticipant(id, entityDetails)
            .map { participant -> ParticipantDto.fromEntity(participant) }
            .map { updatedDto -> ResponseEntity.ok(updatedDto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteParticipant(@PathVariable id: Int): ResponseEntity<Void> {
        return if (participantService.deleteParticipant(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/search/findByLastName")
    fun findParticipantsByLastName(@RequestParam lastName: String): List<ParticipantDto?> {
        return participantService.findParticipantsByLastName(lastName).map(ParticipantDto::fromEntity)
    }

    @GetMapping("/active")
    fun getActiveParticipantsCount(@RequestParam(defaultValue = ParticipantService.SEASON_24_25) season: String): Long {
        return participantService.countActiveParticipants(season)
    }

    @GetMapping("/calcResults")
    fun getParticipantResults(@RequestParam(defaultValue = ParticipantService.SEASON_24_25) season: String): List<ParticipantResultDto> {
        return participantService.calculateResults(season)
    }
}
