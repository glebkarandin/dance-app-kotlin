package danceapp.api.repository

import danceapp.api.model.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AppUserRepository : JpaRepository<AppUser, Int> {
    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.permissions")
    fun findAllWithRoles(): List<AppUser>

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.permissions WHERE u.id = :id")
    fun findByIdWithRoles(@Param("id") id: Int): Optional<AppUser>

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.permissions JOIN u.telegramProfiles tp WHERE tp.telegramId = :telegramId")
    fun findByTelegramId(@Param("telegramId") telegramId: Int): Optional<AppUser>
}
