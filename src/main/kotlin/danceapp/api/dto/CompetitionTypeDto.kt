package danceapp.api.dto

import danceapp.api.model.CompetitionType

data class CompetitionTypeDto(
    val id: Int?,
    val code: String,
    val description: String?
) {
    fun toEntity(): CompetitionType {
        val entity = CompetitionType()
        entity.id = this.id
        entity.code = this.code
        entity.description = this.description
        return entity
    }

    companion object {
        fun fromEntity(entity: CompetitionType?): CompetitionTypeDto? {
            if (entity == null) return null
            return CompetitionTypeDto(entity.id, entity.code, entity.description)
        }
    }
}
