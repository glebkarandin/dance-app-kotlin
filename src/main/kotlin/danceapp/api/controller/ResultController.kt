package danceapp.api.controller

import danceapp.api.dto.ResultCreateUpdateDto
import danceapp.api.dto.ResultDto
import danceapp.api.service.ResultService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bknd/api/results")
class ResultController(private val resultService: ResultService) {

    @GetMapping
    fun getAllResults(): List<ResultDto?> {
        return resultService.getAllResults().map(ResultDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getResultById(@PathVariable id: Int): ResponseEntity<ResultDto> {
        return resultService.getResultById(id)
            .map { result -> ResultDto.fromEntity(result) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createResult(@RequestBody dto: ResultCreateUpdateDto): ResponseEntity<ResultDto> {
        return try {
            val result = dto.toEntity()
            val createdResult = resultService.createResult(
                result, dto.nominationId, dto.participantId
            )
            ResponseEntity.status(HttpStatus.CREATED).body(ResultDto.fromEntity(createdResult))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun updateResult(@PathVariable id: Int, @RequestBody dto: ResultCreateUpdateDto): ResponseEntity<ResultDto> {
        return try {
            val resultDetails = dto.toEntity()
            resultService.updateResult(
                id, resultDetails, dto.nominationId, dto.participantId
            )
                .map { result -> ResultDto.fromEntity(result) }
                .map { updatedDto -> ResponseEntity.ok(updatedDto) }
                .orElse(ResponseEntity.notFound().build())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteResult(@PathVariable id: Int): ResponseEntity<Void> {
        return if (resultService.deleteResult(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/nomination/{nominationId}")
    fun findResultsByNominationId(@PathVariable nominationId: Int): List<ResultDto?> {
        return resultService.findResultsByNominationId(nominationId).map(ResultDto::fromEntity)
    }

    @GetMapping("/participant/{participantId}")
    fun findResultsByParticipantId(@PathVariable participantId: Int): List<ResultDto?> {
        return resultService.findResultsByParticipantId(participantId).map(ResultDto::fromEntity)
    }

    @GetMapping("/nomination/{nominationId}/participant/{participantId}")
    fun findResultsByNominationAndParticipant(
        @PathVariable nominationId: Int, @PathVariable participantId: Int
    ): List<ResultDto?> {
        return resultService.findResultsByNominationAndParticipant(nominationId, participantId).map(ResultDto::fromEntity)
    }
}
