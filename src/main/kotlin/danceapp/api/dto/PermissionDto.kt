package danceapp.api.dto

import danceapp.api.model.Permission

data class PermissionDto(
    val id: Int?,
    val code: String,
    val description: String
) {
    fun toEntity(): Permission {
        val permission = Permission()
        permission.id = this.id
        permission.code = this.code
        permission.description = this.description
        return permission
    }

    companion object {
        fun fromEntity(permission: Permission?): PermissionDto? {
            if (permission == null) return null
            return PermissionDto(permission.id, permission.code, permission.description)
        }
    }
}
