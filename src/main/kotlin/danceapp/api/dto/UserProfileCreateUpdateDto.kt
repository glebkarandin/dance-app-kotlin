package danceapp.api.dto

import danceapp.api.model.UserProfile

data class UserProfileCreateUpdateDto(
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val userId: Int
) {
    fun toEntity(): UserProfile {
        val profile = UserProfile()
        profile.firstName = this.firstName
        profile.lastName = this.lastName
        profile.email = this.email
        // AppUser entity will be set in the service layer using userId
        return profile
    }
}
