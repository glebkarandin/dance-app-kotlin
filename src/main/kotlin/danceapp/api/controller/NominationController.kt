package danceapp.api.controller

import danceapp.api.dto.NominationCreateUpdateDto
import danceapp.api.dto.NominationDto
import danceapp.api.service.NominationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bknd/api/nominations")
class NominationController(private val nominationService: NominationService) {

    @GetMapping
    fun getAllNominations(): List<NominationDto?> {
        return nominationService.getAllNominations().map(NominationDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getNominationById(@PathVariable id: Int): ResponseEntity<NominationDto> {
        return nominationService.getNominationById(id)
            .map { nomination -> NominationDto.fromEntity(nomination) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createNomination(@RequestBody dto: NominationCreateUpdateDto): ResponseEntity<NominationDto> {
        return try {
            val nomination = dto.toEntity()
            val createdNomination = nominationService.createNomination(
                nomination, dto.eventId, dto.typeId, dto.competitionId
            )
            ResponseEntity.status(HttpStatus.CREATED).body(NominationDto.fromEntity(createdNomination))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun updateNomination(@PathVariable id: Int, @RequestBody dto: NominationCreateUpdateDto): ResponseEntity<NominationDto> {
        return try {
            val nominationDetails = dto.toEntity()
            nominationService.updateNomination(
                id, nominationDetails, dto.eventId, dto.typeId, dto.competitionId
            )
                .map { nomination -> NominationDto.fromEntity(nomination) }
                .map { updatedDto -> ResponseEntity.ok(updatedDto) }
                .orElse(ResponseEntity.notFound().build())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteNomination(@PathVariable id: Int): ResponseEntity<Void> {
        return if (nominationService.deleteNomination(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/event/{eventId}")
    fun findNominationsByEventId(@PathVariable eventId: Int): List<NominationDto?> {
        return nominationService.findNominationsByEventId(eventId).map(NominationDto::fromEntity)
    }

    @GetMapping("/type/{typeId}")
    fun findNominationsByNominationTypeId(@PathVariable typeId: Int): List<NominationDto?> {
        return nominationService.findNominationsByNominationTypeId(typeId).map(NominationDto::fromEntity)
    }

    @GetMapping("/competition-type/{competitionTypeId}")
    fun findNominationsByCompetitionTypeId(@PathVariable competitionTypeId: Int): List<NominationDto?> {
        return nominationService.findNominationsByCompetitionTypeId(competitionTypeId).map(NominationDto::fromEntity)
    }
}
