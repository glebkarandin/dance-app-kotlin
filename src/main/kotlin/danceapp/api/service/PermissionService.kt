package danceapp.api.service

import danceapp.api.model.Permission
import danceapp.api.repository.PermissionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
class PermissionService(private val permissionRepository: PermissionRepository) {

    fun getAllPermissions(): List<Permission> {
        return permissionRepository.findAll()
    }

    fun getPermissionById(id: Int): Optional<Permission> {
        return permissionRepository.findById(id)
    }

    fun getPermissionByCode(code: String): Optional<Permission> {
        return permissionRepository.findByCode(code)
    }

    fun createPermission(permission: Permission): Permission {
        return permissionRepository.save(permission)
    }

    fun updatePermission(id: Int, permissionDetails: Permission): Optional<Permission> {
        return permissionRepository.findById(id)
            .map { existingPermission ->
                existingPermission.code = permissionDetails.code
                existingPermission.description = permissionDetails.description
                permissionRepository.save(existingPermission)
            }
    }

    fun deletePermission(id: Int): Boolean {
        if (permissionRepository.existsById(id)) {
            // Consider implications for roles that have this permission.
            permissionRepository.deleteById(id)
            return true
        }
        return false
    }
}
