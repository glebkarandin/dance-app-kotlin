package danceapp.api.dto

import danceapp.api.model.NominationType

data class NominationTypeDto(
    val id: Int?,
    val code: String,
    val description: String?
) {
    fun toEntity(): NominationType {
        val entity = NominationType()
        entity.id = this.id
        entity.code = this.code
        entity.description = this.description
        return entity
    }

    companion object {
        fun fromEntity(entity: NominationType?): NominationTypeDto? {
            if (entity == null) return null
            return NominationTypeDto(entity.id, entity.code, entity.description)
        }
    }
}
