package danceapp.api.dto

import danceapp.api.model.ParticipantProfile

data class ParticipantProfileCreateUpdateDto(
    val participantId: Int
) {
    fun toEntity(): ParticipantProfile {
        val profile = ParticipantProfile()
        // Participant entity itself will be set in the service layer
        return profile
    }
}
