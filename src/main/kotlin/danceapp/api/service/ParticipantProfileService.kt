package danceapp.api.service

import danceapp.api.model.ParticipantProfile
import danceapp.api.repository.ParticipantProfileRepository
import danceapp.api.repository.ParticipantRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ParticipantProfileService(
    private val participantProfileRepository: ParticipantProfileRepository,
    private val participantRepository: ParticipantRepository
) {

    fun getAllParticipantProfiles(): List<ParticipantProfile> {
        return participantProfileRepository.findAll()
    }

    fun getParticipantProfileById(id: Int): Optional<ParticipantProfile> {
        return participantProfileRepository.findById(id)
    }

    fun getParticipantProfileByParticipantId(participantId: Int): Optional<ParticipantProfile> {
        return participantProfileRepository.findByParticipantId(participantId)
    }

    fun createParticipantProfile(participantProfile: ParticipantProfile, participantId: Int): ParticipantProfile {
        val participant = participantRepository.findById(participantId)
            .orElseThrow { IllegalArgumentException("Invalid Participant ID: $participantId") }
        participantProfile.participant = participant
        return participantProfileRepository.save(participantProfile)
    }

    fun updateParticipantProfile(profileId: Int, profileDetails: ParticipantProfile): Optional<ParticipantProfile> {
        return participantProfileRepository.findById(profileId)
            .map { existingProfile ->
                if (profileDetails.participant.id != null) {
                    participantRepository.findById(profileDetails.participant.id!!)
                        .map { p ->
                            existingProfile.participant = p
                            participantProfileRepository.save(existingProfile)
                        }.orElse(existingProfile)
                } else {
                    participantProfileRepository.save(existingProfile)
                }
            }
    }

    fun deleteParticipantProfile(id: Int): Boolean {
        if (participantProfileRepository.existsById(id)) {
            participantProfileRepository.deleteById(id)
            return true
        }
        return false
    }
}
