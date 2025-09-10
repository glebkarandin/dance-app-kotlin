package danceapp.api.service

import danceapp.api.dto.ParticipantResultDto
import danceapp.api.model.Participant
import danceapp.api.model.Result
import danceapp.api.repository.ParticipantRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*
import java.util.stream.Collectors

@Service
@Transactional
class ParticipantService(private val participantRepository: ParticipantRepository) {

    private var currentSeason: String = ""

    companion object {
        private const val COLOR_ACTIVE_1 = "colorActive1"
        private const val COLOR_ACTIVE_2 = "colorActive2"
        private const val COLOR_ACTIVE_3 = "colorActive3"
        private const val COLOR_ACTIVE_4 = "colorActive4"
        private const val COLOR_ACTIVE_5 = "colorActive5"
        const val SEASON_24_25 = "SEASON_24_25"
        const val SEASON_25_26 = "SEASON_25_26"
        private val startDate24_25: OffsetDateTime = OffsetDateTime.of(2024, 9, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        private val endDate24_25: OffsetDateTime = OffsetDateTime.of(2025, 8, 31, 23, 59, 59, 0, ZoneOffset.UTC)
        private val startDate25_26: OffsetDateTime = OffsetDateTime.of(2025, 9, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        private val endDate25_26: OffsetDateTime = OffsetDateTime.of(2026, 8, 31, 23, 59, 59, 0, ZoneOffset.UTC)
    }

    fun getAllParticipants(): List<Participant> {
        return participantRepository.findAll()
    }

    fun getParticipantById(id: Int): Optional<Participant> {
        return participantRepository.findById(id)
    }

    fun createParticipant(participant: Participant): Participant {
        return participantRepository.save(participant)
    }

    fun updateParticipant(id: Int, participantDetails: Participant): Optional<Participant> {
        return participantRepository.findById(id)
            .map { existingParticipant ->
                existingParticipant.firstName = participantDetails.firstName
                existingParticipant.lastName = participantDetails.lastName
                participantRepository.save(existingParticipant)
            }
    }

    fun deleteParticipant(id: Int): Boolean {
        if (participantRepository.existsById(id)) {
            participantRepository.deleteById(id)
            return true
        }
        return false
    }

    fun findParticipantsByLastName(lastName: String): List<Participant> {
        return participantRepository.findByLastName(lastName)
    }

    fun countActiveParticipants(season: String): Long {
        return if (season == SEASON_24_25) {
            participantRepository.countActiveParticipantsInDateRange(startDate24_25, endDate24_25)
        } else {
            participantRepository.countActiveParticipantsInDateRange(startDate25_26, endDate25_26)
        }
    }

    fun calculateResults(season: String): List<ParticipantResultDto> {
        this.currentSeason = season
        val participants = if (season == SEASON_24_25) {
            participantRepository.findParticipantsByEventDateRange(startDate24_25, endDate24_25)
        } else {
            participantRepository.findParticipantsByEventDateRange(startDate25_26, endDate25_26)
        }

        val results = participants.map { calculate(it) }

        val maxJnjPoints = results.mapNotNull { it.nominationsCount["JNJ"] }.maxOrNull() ?: 0
        if (maxJnjPoints > 0) {
            results.filter { it.nominationsCount["JNJ"] == maxJnjPoints }.forEach { it.topJNJ = true }
        }

        val maxStrictlyPoints = results.mapNotNull { it.nominationsCount["STRICTLY"] }.maxOrNull() ?: 0
        if (maxStrictlyPoints > 0) {
            results.filter { it.nominationsCount["STRICTLY"] == maxStrictlyPoints }.forEach { it.topStrictly = true }
        }

        results.forEach { result ->
            val points = result.points
            result.indicatorColor = when {
                points <= 29 -> COLOR_ACTIVE_1
                points <= 59 -> COLOR_ACTIVE_2
                points <= 99 -> COLOR_ACTIVE_3
                points <= 249 -> COLOR_ACTIVE_4
                else -> COLOR_ACTIVE_5
            }
        }

        return results.filter { it.points > 0 }
            .sortedWith(compareByDescending<ParticipantResultDto> { it.points }.thenBy { it.name })
    }

    private fun calculate(participant: Participant): ParticipantResultDto {
        val nominationsCount = mutableMapOf<String, Int>()
        val competitionTypeCodes = listOf("JNJ", "STRICTLY", "CABARET", "EVENT_SHOW", "ROUTINE", "SHOW")
        competitionTypeCodes.forEach { code -> nominationsCount[code] = 0 }
        var points = 0

        for (result in participant.results) {
            val code = result.nomination.competitionType.code
            nominationsCount.computeIfPresent(code) { _, v -> v + 1 }
            if (currentSeason == SEASON_24_25) {
                points += when (code) {
                    "JNJ" -> 10
                    "STRICTLY" -> 20
                    "CABARET" -> 30
                    "EVENT_SHOW" -> 40
                    "ROUTINE" -> 50
                    "SHOW" -> 60
                    else -> 0
                }
            }
            if (currentSeason == SEASON_25_26) {
                points += when (code) {
                    "JNJ" -> 10
                    "STRICTLY" -> 20
                    "EVENT_SHOW" -> 40
                    "ROUTINE" -> 50
                    "CABARET" -> 60
                    "SHOW" -> 70
                    else -> 0
                }
            }
        }

        if (currentSeason == SEASON_25_26) {
            val jnjCount = nominationsCount.getOrDefault("JNJ", 0)
            points += jnjCount / 3 * 50
        }

        return ParticipantResultDto(
            name = "${participant.lastName} ${participant.firstName}",
            id = participant.id!!,
            nominationsCount = nominationsCount,
            points = points
        )
    }
}
