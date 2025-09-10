package danceapp.api.dto

import danceapp.api.model.Nomination

data class NominationCreateUpdateDto(
    val title: String,
    val eventId: Int,
    val typeId: Int, // nominationTypeId
    val competitionId: Int // competitionTypeId
) {
    fun toEntity(): Nomination {
        val nomination = Nomination()
        nomination.title = this.title
        // The actual Event, NominationType, CompetitionType entities will be set in the service layer
        // using the IDs provided here.
        return nomination
    }
}
