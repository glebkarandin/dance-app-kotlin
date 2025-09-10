package danceapp.api.dto

import danceapp.api.model.ParticipantProfile

data class ParticipantProfileDto(
    val id: Int?,
    val participantId: Int?
) {
    companion object {
        fun fromEntity(entity: ParticipantProfile?): ParticipantProfileDto? {
            if (entity == null) return null
            return ParticipantProfileDto(
                entity.id,
                entity.participant.id
            )
        }
    }
}
