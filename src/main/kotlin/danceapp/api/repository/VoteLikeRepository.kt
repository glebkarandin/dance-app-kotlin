package danceapp.api.repository

import danceapp.api.model.VoteLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VoteLikeRepository : JpaRepository<VoteLike, Int> {
    fun findByUserId(userId: Int): List<VoteLike>
    fun findByParticipantId(participantId: Int): List<VoteLike>
    fun findByVotesThemeId(votesThemeId: Int): List<VoteLike>
    fun findByUserIdAndParticipantIdAndVotesThemeIdAndPeriodMark(userId: Int, participantId: Int, votesThemeId: Int, periodMark: String): List<VoteLike>
}
