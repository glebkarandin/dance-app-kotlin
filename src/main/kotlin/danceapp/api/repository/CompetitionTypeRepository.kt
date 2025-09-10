package danceapp.api.repository

import danceapp.api.model.CompetitionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface CompetitionTypeRepository : JpaRepository<CompetitionType, Int> {
    fun findByCode(code: String): Optional<CompetitionType>
}
