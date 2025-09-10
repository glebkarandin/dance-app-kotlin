package danceapp.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import danceapp.api.model.VoteLike
import java.time.LocalDateTime

data class VoteLikeDto(
    val id: Int?,
    val userId: Int?,
    val participantId: Int?,
    val votesThemeId: Int?,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime,
    val periodMark: String
) {
    companion object {
        fun fromEntity(entity: VoteLike?): VoteLikeDto? {
            if (entity == null) return null
            return VoteLikeDto(
                entity.id,
                entity.user.id,
                entity.participant.id,
                entity.votesTheme.id,
                entity.createdAt,
                entity.periodMark
            )
        }
    }
}
