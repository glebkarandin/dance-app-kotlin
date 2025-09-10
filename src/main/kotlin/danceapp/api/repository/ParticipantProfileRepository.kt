package danceapp.api.repository

import danceapp.api.model.ParticipantProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ParticipantProfileRepository : JpaRepository<ParticipantProfile, Int> {
    fun findByParticipantId(participantId: Int): Optional<ParticipantProfile>
}
