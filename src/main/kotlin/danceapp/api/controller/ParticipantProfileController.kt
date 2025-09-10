package danceapp.api.controller

import danceapp.api.dto.ParticipantProfileCreateUpdateDto
import danceapp.api.dto.ParticipantProfileDto
import danceapp.api.model.ParticipantProfile
import danceapp.api.service.ParticipantProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bknd/api/participant-profiles")
class ParticipantProfileController(private val participantProfileService: ParticipantProfileService) {

    @GetMapping
    fun getAllParticipantProfiles(): List<ParticipantProfileDto?> {
        return participantProfileService.getAllParticipantProfiles().map(ParticipantProfileDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getParticipantProfileById(@PathVariable id: Int): ResponseEntity<ParticipantProfileDto> {
        return participantProfileService.getParticipantProfileById(id)
            .map { profile -> ParticipantProfileDto.fromEntity(profile) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/participant/{participantId}")
    fun getParticipantProfileByParticipantId(@PathVariable participantId: Int): ResponseEntity<ParticipantProfileDto> {
        return participantProfileService.getParticipantProfileByParticipantId(participantId)
            .map { profile -> ParticipantProfileDto.fromEntity(profile) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createParticipantProfile(@RequestBody dto: ParticipantProfileCreateUpdateDto): ResponseEntity<ParticipantProfileDto> {
        return try {
            val profile = dto.toEntity()
            val createdProfile = participantProfileService.createParticipantProfile(profile, dto.participantId)
            ResponseEntity.status(HttpStatus.CREATED).body(ParticipantProfileDto.fromEntity(createdProfile))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun updateParticipantProfile(@PathVariable id: Int, @RequestBody dto: ParticipantProfileCreateUpdateDto): ResponseEntity<ParticipantProfileDto> {
        val profileDetails = dto.toEntity()
        return participantProfileService.updateParticipantProfile(id, profileDetails)
            .map { profile -> ParticipantProfileDto.fromEntity(profile) }
            .map { updatedDto -> ResponseEntity.ok(updatedDto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteParticipantProfile(@PathVariable id: Int): ResponseEntity<Void> {
        return if (participantProfileService.deleteParticipantProfile(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
