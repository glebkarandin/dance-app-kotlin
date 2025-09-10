package danceapp.api.dto

import danceapp.api.model.UserProfile

data class UserProfileDto(
    val id: Int?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val userId: Int?
) {
    companion object {
        fun fromEntity(entity: UserProfile?): UserProfileDto? {
            if (entity == null) return null
            return UserProfileDto(
                entity.id,
                entity.firstName,
                entity.lastName,
                entity.email,
                entity.user.id
            )
        }
    }
}
