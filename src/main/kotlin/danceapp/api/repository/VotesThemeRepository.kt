package danceapp.api.repository

import danceapp.api.model.VotesTheme
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface VotesThemeRepository : JpaRepository<VotesTheme, Int> {
    fun findByThemeCode(themeCode: String): Optional<VotesTheme>
}
