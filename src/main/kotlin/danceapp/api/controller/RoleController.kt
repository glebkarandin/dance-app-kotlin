package danceapp.api.controller

import danceapp.api.dto.PermissionDto
import danceapp.api.dto.RoleDto
import danceapp.api.model.Role
import danceapp.api.service.RoleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/bknd/api/roles")
class RoleController(private val roleService: RoleService) {

    @GetMapping
    fun getAllRoles(): List<RoleDto?> {
        return roleService.getAllRoles().stream()
            .map(RoleDto::fromEntity)
            .collect(Collectors.toList())
    }

    @GetMapping("/{id}")
    fun getRoleById(@PathVariable id: Int): ResponseEntity<RoleDto> {
        return roleService.getRoleById(id)
            .map { role -> RoleDto.fromEntity(role) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/code/{code}")
    fun getRoleByCode(@PathVariable code: String): ResponseEntity<RoleDto> {
        return roleService.getRoleByCode(code)
            .map { role -> RoleDto.fromEntity(role) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createRole(@RequestBody roleDto: RoleDto): ResponseEntity<RoleDto> {
        val role = roleDto.toEntity()
        return try {
            val permissionIds = roleDto.permissions.mapNotNull { it.id }.toSet()
            val createdRole = roleService.createRole(role, permissionIds)
            ResponseEntity.status(HttpStatus.CREATED).body(RoleDto.fromEntity(createdRole))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun updateRole(@PathVariable id: Int, @RequestBody roleDto: RoleDto): ResponseEntity<RoleDto> {
        val roleDetails = roleDto.toEntity()
        return try {
            val permissionIds = roleDto.permissions.mapNotNull { it.id }.toSet()
            roleService.updateRole(id, roleDetails, permissionIds)
                .map { updatedRole -> RoleDto.fromEntity(updatedRole) }
                .map { updatedDto -> ResponseEntity.ok(updatedDto) }
                .orElse(ResponseEntity.notFound().build())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteRole(@PathVariable id: Int): ResponseEntity<Void> {
        return if (roleService.deleteRole(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
