package danceapp.api.repository

import danceapp.api.model.TelegramProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TelegramProfileRepository : JpaRepository<TelegramProfile, Int> {
    fun findByUserId(userId: Int): Optional<TelegramProfile>
    fun findByTelegramId(telegramId: Int): Optional<TelegramProfile>
    fun findByUserName(userName: String): Optional<TelegramProfile>
}
