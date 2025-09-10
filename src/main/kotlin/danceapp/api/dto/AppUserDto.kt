package danceapp.api.dto

import danceapp.api.model.AppUser

data class AppUserDto(
    var id: Int? = null,
    var roles: Set<RoleDto> = HashSet()
) {
    companion object {
        fun fromEntity(appUser: AppUser?): AppUserDto? {
            if (appUser == null) return null
            val roleDtos = appUser.roles.mapNotNull { RoleDto.fromEntity(it) }.toSet()
            return AppUserDto(appUser.id, roleDtos)
        }
    }
}
