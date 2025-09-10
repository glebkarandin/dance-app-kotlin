package danceapp.api.dto

import danceapp.api.model.Participant

data class ParticipantDto(
    val id: Int?,
    val firstName: String?,
    val lastName: String?
) {
    fun toEntity(): Participant {
        val entity = Participant()
        entity.id = this.id
        entity.firstName = this.firstName
        entity.lastName = this.lastName
        return entity
    }

    companion object {
        fun fromEntity(entity: Participant?): ParticipantDto? {
            if (entity == null) return null
            return ParticipantDto(entity.id, entity.firstName, entity.lastName)
        }
    }
}
