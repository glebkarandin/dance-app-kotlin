package danceapp.api.service

import danceapp.api.model.Result
import danceapp.api.repository.NominationRepository
import danceapp.api.repository.ParticipantRepository
import danceapp.api.repository.ResultRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ResultService(
    private val resultRepository: ResultRepository,
    private val nominationRepository: NominationRepository,
    private val participantRepository: ParticipantRepository
) {

    fun getAllResults(): List<Result> {
        return resultRepository.findAllWithDetails()
    }

    fun getResultById(id: Int): Optional<Result> {
        return resultRepository.findById(id)
    }

    fun createResult(result: Result, nominationId: Int, participantId: Int): Result {
        val nomination = nominationRepository.findById(nominationId)
            .orElseThrow { IllegalArgumentException("Invalid Nomination ID") }
        val participant = participantRepository.findById(participantId)
            .orElseThrow { IllegalArgumentException("Invalid Participant ID") }

        result.nomination = nomination
        result.participant = participant
        return resultRepository.save(result)
    }

    fun updateResult(id: Int, resultDetails: Result, nominationId: Int, participantId: Int): Optional<Result> {
        return resultRepository.findById(id)
            .map { existingResult ->
                val nomination = nominationRepository.findById(nominationId)
                    .orElseThrow { IllegalArgumentException("Invalid Nomination ID") }
                val participant = participantRepository.findById(participantId)
                    .orElseThrow { IllegalArgumentException("Invalid Participant ID") }

                existingResult.place = resultDetails.place
                existingResult.nomination = nomination
                existingResult.participant = participant
                resultRepository.save(existingResult)
            }
    }

    fun deleteResult(id: Int): Boolean {
        if (resultRepository.existsById(id)) {
            resultRepository.deleteById(id)
            return true
        }
        return false
    }

    fun findResultsByNominationId(nominationId: Int): List<Result> {
        return resultRepository.findByNominationId(nominationId)
    }

    fun findResultsByParticipantId(participantId: Int): List<Result> {
        return resultRepository.findByParticipantId(participantId)
    }

    fun findResultsByNominationAndParticipant(nominationId: Int, participantId: Int): List<Result> {
        return resultRepository.findByNominationIdAndParticipantId(nominationId, participantId)
    }
}
