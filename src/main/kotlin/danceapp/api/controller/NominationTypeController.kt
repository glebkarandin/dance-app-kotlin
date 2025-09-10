package danceapp.api.controller

import danceapp.api.dto.NominationTypeDto
import danceapp.api.service.NominationTypeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bknd/api/nomination-types")
class NominationTypeController(private val nominationTypeService: NominationTypeService) {

    @GetMapping
    fun getAllNominationTypes(): List<NominationTypeDto?> {
        return nominationTypeService.getAllNominationTypes().map(NominationTypeDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getNominationTypeById(@PathVariable id: Int): ResponseEntity<NominationTypeDto> {
        return nominationTypeService.getNominationTypeById(id)
            .map { type -> NominationTypeDto.fromEntity(type) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/code/{code}")
    fun getNominationTypeByCode(@PathVariable code: String): ResponseEntity<NominationTypeDto> {
        return nominationTypeService.getNominationTypeByCode(code)
            .map { type -> NominationTypeDto.fromEntity(type) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createNominationType(@RequestBody dto: NominationTypeDto): ResponseEntity<NominationTypeDto> {
        val entity = dto.toEntity()
        val createdEntity = nominationTypeService.createNominationType(entity)
        return ResponseEntity.status(HttpStatus.CREATED).body(NominationTypeDto.fromEntity(createdEntity))
    }

    @PutMapping("/{id}")
    fun updateNominationType(@PathVariable id: Int, @RequestBody dto: NominationTypeDto): ResponseEntity<NominationTypeDto> {
        val entityDetails = dto.toEntity()
        return nominationTypeService.updateNominationType(id, entityDetails)
            .map { type -> NominationTypeDto.fromEntity(type) }
            .map { updatedDto -> ResponseEntity.ok(updatedDto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteNominationType(@PathVariable id: Int): ResponseEntity<Void> {
        return if (nominationTypeService.deleteNominationType(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
