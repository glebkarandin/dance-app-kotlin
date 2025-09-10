package danceapp.api.dto

import danceapp.api.model.Result

data class ResultCreateUpdateDto(
    val place: Int,
    val nominationId: Int,
    val participantId: Int
) {
    fun toEntity(): Result {
        val result = Result()
        result.place = this.place
        // Nomination and Participant entities will be set in the service layer
        return result
    }
}
