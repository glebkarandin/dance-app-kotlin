package danceapp.api.controller

import danceapp.api.dto.PermissionDto
import danceapp.api.service.PermissionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bknd/api/permissions")
class PermissionController(private val permissionService: PermissionService) {

    @GetMapping
    fun getAllPermissions(): List<PermissionDto?> {
        return permissionService.getAllPermissions().map(PermissionDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getPermissionById(@PathVariable id: Int): ResponseEntity<PermissionDto> {
        return permissionService.getPermissionById(id)
            .map { permission -> PermissionDto.fromEntity(permission) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/code/{code}")
    fun getPermissionByCode(@PathVariable code: String): ResponseEntity<PermissionDto> {
        return permissionService.getPermissionByCode(code)
            .map { permission -> PermissionDto.fromEntity(permission) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createPermission(@RequestBody permissionDto: PermissionDto): ResponseEntity<PermissionDto> {
        val permission = permissionDto.toEntity()
        val createdPermission = permissionService.createPermission(permission)
        return ResponseEntity.status(HttpStatus.CREATED).body(PermissionDto.fromEntity(createdPermission))
    }

    @PutMapping("/{id}")
    fun updatePermission(@PathVariable id: Int, @RequestBody permissionDto: PermissionDto): ResponseEntity<PermissionDto> {
        val permissionDetails = permissionDto.toEntity()
        return permissionService.updatePermission(id, permissionDetails)
            .map { permission -> PermissionDto.fromEntity(permission) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deletePermission(@PathVariable id: Int): ResponseEntity<Void> {
        return if (permissionService.deletePermission(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
