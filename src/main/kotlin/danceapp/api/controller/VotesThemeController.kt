package danceapp.api.controller

import danceapp.api.dto.VotesThemeDto
import danceapp.api.service.VotesThemeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bknd/api/votes-themes")
class VotesThemeController(private val votesThemeService: VotesThemeService) {

    @GetMapping
    fun getAllVotesThemes(): List<VotesThemeDto?> {
        return votesThemeService.getAllVotesThemes().map(VotesThemeDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getVotesThemeById(@PathVariable id: Int): ResponseEntity<VotesThemeDto> {
        return votesThemeService.getVotesThemeById(id)
            .map { theme -> VotesThemeDto.fromEntity(theme) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/code/{themeCode}")
    fun getVotesThemeByThemeCode(@PathVariable themeCode: String): ResponseEntity<VotesThemeDto> {
        return votesThemeService.getVotesThemeByThemeCode(themeCode)
            .map { theme -> VotesThemeDto.fromEntity(theme) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createVotesTheme(@RequestBody dto: VotesThemeDto): ResponseEntity<VotesThemeDto> {
        val entity = dto.toEntity()
        val createdEntity = votesThemeService.createVotesTheme(entity)
        return ResponseEntity.status(HttpStatus.CREATED).body(VotesThemeDto.fromEntity(createdEntity))
    }

    @PutMapping("/{id}")
    fun updateVotesTheme(@PathVariable id: Int, @RequestBody dto: VotesThemeDto): ResponseEntity<VotesThemeDto> {
        val entityDetails = dto.toEntity()
        return votesThemeService.updateVotesTheme(id, entityDetails)
            .map { theme -> VotesThemeDto.fromEntity(theme) }
            .map { updatedDto -> ResponseEntity.ok(updatedDto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteVotesTheme(@PathVariable id: Int): ResponseEntity<Void> {
        return if (votesThemeService.deleteVotesTheme(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
