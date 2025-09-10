package danceapp.api.dto

import danceapp.api.model.TelegramProfile

data class TelegramProfileCreateUpdateDto(
    val firstName: String,
    val lastName: String?,
    val userName: String,
    val telegramId: Int,
    val userId: Int
) {
    fun toEntity(): TelegramProfile {
        val profile = TelegramProfile()
        profile.firstName = this.firstName
        profile.lastName = this.lastName ?: ""
        profile.userName = this.userName
        profile.telegramId = this.telegramId
        // AppUser entity will be set in the service layer using userId
        return profile
    }
}
