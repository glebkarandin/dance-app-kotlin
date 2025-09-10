package danceapp.api.dto

import danceapp.api.model.Result

data class ResultDto(
    val id: Int?,
    val place: Int,
    val nomination: NominationDto?,
    val participant: ParticipantDto?
) {
    companion object {
        fun fromEntity(entity: Result?): ResultDto? {
            if (entity == null) return null
            return ResultDto(
                entity.id,
                entity.place,
                NominationDto.fromEntity(entity.nomination),
                ParticipantDto.fromEntity(entity.participant)
            )
        }
    }
}
