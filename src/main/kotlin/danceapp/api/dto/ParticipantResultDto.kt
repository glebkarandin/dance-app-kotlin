package danceapp.api.dto

data class ParticipantResultDto(
    var name: String,
    var id: Int,
    var nominationsCount: Map<String, Int>,
    var points: Int,
    var topJNJ: Boolean? = null,
    var topStrictly: Boolean? = null,
    var indicatorColor: String? = null
)
