package danceapp.api.dto

import danceapp.api.model.VotesTheme

data class VotesThemeDto(
    val id: Int?,
    val themeCode: String,
    val description: String,
    val periodType: String
) {
    fun toEntity(): VotesTheme {
        val entity = VotesTheme()
        entity.id = this.id
        entity.themeCode = this.themeCode
        entity.description = this.description
        entity.periodType = this.periodType
        return entity
    }

    companion object {
        fun fromEntity(entity: VotesTheme?): VotesThemeDto? {
            if (entity == null) return null
            return VotesThemeDto(entity.id, entity.themeCode, entity.description, entity.periodType)
        }
    }
}
