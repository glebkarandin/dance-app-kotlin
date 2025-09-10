package danceapp.api.service

import danceapp.api.model.VoteLike
import danceapp.api.repository.AppUserRepository
import danceapp.api.repository.ParticipantRepository
import danceapp.api.repository.VoteLikeRepository
import danceapp.api.repository.VotesThemeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class VoteLikeService(
    private val voteLikeRepository: VoteLikeRepository,
    private val appUserRepository: AppUserRepository,
    private val participantRepository: ParticipantRepository,
    private val votesThemeRepository: VotesThemeRepository
) {

    fun getAllVoteLikes(): List<VoteLike> {
        return voteLikeRepository.findAll()
    }

    fun getVoteLikeById(id: Int): Optional<VoteLike> {
        return voteLikeRepository.findById(id)
    }

    fun createVoteLike(voteLike: VoteLike, userId: Int, participantId: Int, votesThemeId: Int): VoteLike {
        val user = appUserRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("Invalid User ID") }
        val participant = participantRepository.findById(participantId)
            .orElseThrow { IllegalArgumentException("Invalid Participant ID") }
        val theme = votesThemeRepository.findById(votesThemeId)
            .orElseThrow { IllegalArgumentException("Invalid VotesTheme ID") }

        voteLike.user = user
        voteLike.participant = participant
        voteLike.votesTheme = theme
        return voteLikeRepository.save(voteLike)
    }

    fun updateVoteLike(id: Int, voteLikeDetails: VoteLike, userId: Int, participantId: Int, votesThemeId: Int): Optional<VoteLike> {
        return voteLikeRepository.findById(id)
            .map { existingVoteLike ->
                val user = appUserRepository.findById(userId)
                    .orElseThrow { IllegalArgumentException("Invalid User ID") }
                val participant = participantRepository.findById(participantId)
                    .orElseThrow { IllegalArgumentException("Invalid Participant ID") }
                val theme = votesThemeRepository.findById(votesThemeId)
                    .orElseThrow { IllegalArgumentException("Invalid VotesTheme ID") }

                existingVoteLike.user = user
                existingVoteLike.participant = participant
                existingVoteLike.votesTheme = theme
                existingVoteLike.periodMark = voteLikeDetails.periodMark
                voteLikeRepository.save(existingVoteLike)
            }
    }

    fun deleteVoteLike(id: Int): Boolean {
        if (voteLikeRepository.existsById(id)) {
            voteLikeRepository.deleteById(id)
            return true
        }
        return false
    }

    fun findVoteLikesByUserId(userId: Int): List<VoteLike> {
        return voteLikeRepository.findByUserId(userId)
    }

    fun findVoteLikesByParticipantId(participantId: Int): List<VoteLike> {
        return voteLikeRepository.findByParticipantId(participantId)
    }

    fun findVoteLikesByVotesThemeId(votesThemeId: Int): List<VoteLike> {
        return voteLikeRepository.findByVotesThemeId(votesThemeId)
    }

    fun findSpecificVoteLike(userId: Int, participantId: Int, votesThemeId: Int, periodMark: String): List<VoteLike> {
        return voteLikeRepository.findByUserIdAndParticipantIdAndVotesThemeIdAndPeriodMark(userId, participantId, votesThemeId, periodMark)
    }
}
