package danceapp.api.dto

import danceapp.api.model.AppUser

data class AuthResponseDto(
    var id: Int?,
    var roles: Set<RoleDto>
) {
    companion object {
        fun fromEntity(user: AppUser?): AuthResponseDto? {
            if (user == null) return null
            val roles = user.roles.mapNotNull { RoleDto.fromEntity(it) }.toSet()
            return AuthResponseDto(user.id, roles)
        }
    }
}
