package danceapp.api.dto

import danceapp.api.model.VoteLike

data class VoteLikeCreateUpdateDto(
    val userId: Int,
    val participantId: Int,
    val votesThemeId: Int,
    val periodMark: String
) {
    fun toEntity(): VoteLike {
        val voteLike = VoteLike()
        voteLike.periodMark = this.periodMark
        // User, Participant, VotesTheme will be set in the service layer
        return voteLike
    }
}
