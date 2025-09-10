package danceapp.api.repository

import danceapp.api.model.NominationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface NominationTypeRepository : JpaRepository<NominationType, Int> {
    fun findByCode(code: String): Optional<NominationType>
}
