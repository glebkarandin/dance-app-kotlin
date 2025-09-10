package danceapp.api.dto

import danceapp.api.model.Nomination

data class NominationDto(
    val id: Int?,
    val title: String,
    val event: EventDto?,
    val nominationType: NominationTypeDto?,
    val competitionType: CompetitionTypeDto?
) {
    companion object {
        fun fromEntity(entity: Nomination?): NominationDto? {
            if (entity == null) return null
            return NominationDto(
                entity.id,
                entity.title,
                EventDto.fromEntity(entity.event),
                NominationTypeDto.fromEntity(entity.nominationType),
                CompetitionTypeDto.fromEntity(entity.competitionType)
            )
        }
    }
}
