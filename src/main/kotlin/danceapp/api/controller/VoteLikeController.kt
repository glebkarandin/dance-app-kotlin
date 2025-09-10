package danceapp.api.controller

import danceapp.api.dto.VoteLikeCreateUpdateDto
import danceapp.api.dto.VoteLikeDto
import danceapp.api.service.VoteLikeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bknd/api/vote-likes")
class VoteLikeController(private val voteLikeService: VoteLikeService) {

    @GetMapping
    fun getAllVoteLikes(): List<VoteLikeDto?> {
        return voteLikeService.getAllVoteLikes().map(VoteLikeDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getVoteLikeById(@PathVariable id: Int): ResponseEntity<VoteLikeDto> {
        return voteLikeService.getVoteLikeById(id)
            .map { voteLike -> VoteLikeDto.fromEntity(voteLike) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createVoteLike(@RequestBody dto: VoteLikeCreateUpdateDto): ResponseEntity<VoteLikeDto> {
        return try {
            val voteLike = dto.toEntity()
            val createdVoteLike = voteLikeService.createVoteLike(
                voteLike, dto.userId, dto.participantId, dto.votesThemeId
            )
            ResponseEntity.status(HttpStatus.CREATED).body(VoteLikeDto.fromEntity(createdVoteLike))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun updateVoteLike(@PathVariable id: Int, @RequestBody dto: VoteLikeCreateUpdateDto): ResponseEntity<VoteLikeDto> {
        return try {
            val voteLikeDetails = dto.toEntity()
            voteLikeService.updateVoteLike(
                id, voteLikeDetails, dto.userId, dto.participantId, dto.votesThemeId
            )
                .map { voteLike -> VoteLikeDto.fromEntity(voteLike) }
                .map { updatedDto -> ResponseEntity.ok(updatedDto) }
                .orElse(ResponseEntity.notFound().build())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteVoteLike(@PathVariable id: Int): ResponseEntity<Void> {
        return if (voteLikeService.deleteVoteLike(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/user/{userId}")
    fun findVoteLikesByUserId(@PathVariable userId: Int): List<VoteLikeDto?> {
        return voteLikeService.findVoteLikesByUserId(userId).map(VoteLikeDto::fromEntity)
    }

    @GetMapping("/participant/{participantId}")
    fun findVoteLikesByParticipantId(@PathVariable participantId: Int): List<VoteLikeDto?> {
        return voteLikeService.findVoteLikesByParticipantId(participantId).map(VoteLikeDto::fromEntity)
    }

    @GetMapping("/theme/{votesThemeId}")
    fun findVoteLikesByVotesThemeId(@PathVariable votesThemeId: Int): List<VoteLikeDto?> {
        return voteLikeService.findVoteLikesByVotesThemeId(votesThemeId).map(VoteLikeDto::fromEntity)
    }

    @GetMapping("/search/specific")
    fun findSpecificVoteLike(
        @RequestParam userId: Int,
        @RequestParam participantId: Int,
        @RequestParam votesThemeId: Int,
        @RequestParam periodMark: String
    ): List<VoteLikeDto?> {
        return voteLikeService.findSpecificVoteLike(userId, participantId, votesThemeId, periodMark).map(VoteLikeDto::fromEntity)
    }
}
