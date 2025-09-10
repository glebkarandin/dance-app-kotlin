package danceapp.api.repository

import danceapp.api.model.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserProfileRepository : JpaRepository<UserProfile, Int> {
    fun findByUserId(userId: Int): Optional<UserProfile>
    fun findByEmail(email: String): Optional<UserProfile>
}
