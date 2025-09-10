package danceapp.api.dto

import danceapp.api.model.Role

data class RoleDto(
    val id: Int?,
    val code: String,
    val description: String,
    val permissions: Set<PermissionDto>
) {
    fun toEntity(): Role {
        val role = Role()
        role.id = this.id
        role.code = this.code
        role.description = this.description
        // Note: Setting permissions would require fetching Permission entities by IDs.
        // This is typically done in the service layer when handling DTOs for create/update.
        return role
    }

    companion object {
        fun fromEntity(role: Role?): RoleDto? {
            if (role == null) return null
            val perms = role.permissions.mapNotNull { PermissionDto.fromEntity(it) }.toSet()
            return RoleDto(role.id, role.code, role.description, perms)
        }
    }
}
