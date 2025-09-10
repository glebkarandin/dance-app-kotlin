package danceapp.api.controller

import danceapp.api.dto.CompetitionTypeDto
import danceapp.api.service.CompetitionTypeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bknd/api/competition-types")
class CompetitionTypeController(private val competitionTypeService: CompetitionTypeService) {

    @GetMapping
    fun getAllCompetitionTypes(): List<CompetitionTypeDto?> {
        return competitionTypeService.getAllCompetitionTypes().map(CompetitionTypeDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getCompetitionTypeById(@PathVariable id: Int): ResponseEntity<CompetitionTypeDto> {
        return competitionTypeService.getCompetitionTypeById(id)
            .map { type -> CompetitionTypeDto.fromEntity(type) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/code/{code}")
    fun getCompetitionTypeByCode(@PathVariable code: String): ResponseEntity<CompetitionTypeDto> {
        return competitionTypeService.getCompetitionTypeByCode(code)
            .map { type -> CompetitionTypeDto.fromEntity(type) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createCompetitionType(@RequestBody dto: CompetitionTypeDto): ResponseEntity<CompetitionTypeDto> {
        val entity = dto.toEntity()
        val createdEntity = competitionTypeService.createCompetitionType(entity)
        return ResponseEntity.status(HttpStatus.CREATED).body(CompetitionTypeDto.fromEntity(createdEntity))
    }

    @PutMapping("/{id}")
    fun updateCompetitionType(@PathVariable id: Int, @RequestBody dto: CompetitionTypeDto): ResponseEntity<CompetitionTypeDto> {
        val entityDetails = dto.toEntity()
        return competitionTypeService.updateCompetitionType(id, entityDetails)
            .map { type -> CompetitionTypeDto.fromEntity(type) }
            .map { updatedDto -> ResponseEntity.ok(updatedDto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteCompetitionType(@PathVariable id: Int): ResponseEntity<Void> {
        return if (competitionTypeService.deleteCompetitionType(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
