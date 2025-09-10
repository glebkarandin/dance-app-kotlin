package danceapp.api.service

import danceapp.api.model.Permission
import danceapp.api.model.Role
import danceapp.api.repository.PermissionRepository
import danceapp.api.repository.RoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class RoleService(
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository
) {

    fun getAllRoles(): List<Role> {
        return roleRepository.findAll()
    }

    fun getRoleById(id: Int): Optional<Role> {
        return roleRepository.findById(id)
    }

    fun getRoleByCode(code: String): Optional<Role> {
        return roleRepository.findByCode(code)
    }

    fun createRole(role: Role, permissionIds: Set<Int>?): Role {
        if (permissionIds != null && permissionIds.isNotEmpty()) {
            val permissions = permissionRepository.findAllById(permissionIds).toMutableSet()
            if (permissions.size != permissionIds.size) {
                throw IllegalArgumentException("One or more permission IDs are invalid.")
            }
            role.permissions = permissions
        } else {
            role.permissions = HashSet()
        }
        return roleRepository.save(role)
    }

    fun updateRole(id: Int, roleDetails: Role, permissionIds: Set<Int>?): Optional<Role> {
        return roleRepository.findById(id)
            .map { existingRole ->
                existingRole.code = roleDetails.code
                existingRole.description = roleDetails.description

                if (permissionIds != null) {
                    if (permissionIds.isEmpty()) {
                        existingRole.permissions = HashSet()
                    } else {
                        val permissions = permissionRepository.findAllById(permissionIds).toMutableSet()
                        if (permissions.size != permissionIds.size) {
                            throw IllegalArgumentException("One or more permission IDs are invalid for update.")
                        }
                        existingRole.permissions = permissions
                    }
                }
                roleRepository.save(existingRole)
            }
    }

    fun deleteRole(id: Int): Boolean {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id)
            return true
        }
        return false
    }
}
