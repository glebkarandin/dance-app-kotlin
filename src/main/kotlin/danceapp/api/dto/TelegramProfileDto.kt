package danceapp.api.dto

import danceapp.api.model.TelegramProfile

data class TelegramProfileDto(
    val id: Int?,
    val firstName: String,
    val lastName: String,
    val userName: String,
    val telegramId: Int,
    val userId: Int?
) {
    companion object {
        fun fromEntity(entity: TelegramProfile?): TelegramProfileDto? {
            if (entity == null) return null
            return TelegramProfileDto(
                entity.id,
                entity.firstName,
                entity.lastName,
                entity.userName,
                entity.telegramId,
                entity.user.id
            )
        }
    }
}
