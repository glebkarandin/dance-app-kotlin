package danceapp.api.service

import danceapp.api.model.Nomination
import danceapp.api.repository.CompetitionTypeRepository
import danceapp.api.repository.EventRepository
import danceapp.api.repository.NominationRepository
import danceapp.api.repository.NominationTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class NominationService(
    private val nominationRepository: NominationRepository,
    private val eventRepository: EventRepository,
    private val nominationTypeRepository: NominationTypeRepository,
    private val competitionTypeRepository: CompetitionTypeRepository
) {

    fun getAllNominations(): List<Nomination> {
        return nominationRepository.findAllWithDetails()
    }

    fun getNominationById(id: Int): Optional<Nomination> {
        return nominationRepository.findById(id)
    }

    fun createNomination(nomination: Nomination, eventId: Int, typeId: Int, competitionId: Int): Nomination {
        val event = eventRepository.findById(eventId)
            .orElseThrow { IllegalArgumentException("Invalid Event ID") }
        val nominationType = nominationTypeRepository.findById(typeId)
            .orElseThrow { IllegalArgumentException("Invalid NominationType ID") }
        val competitionType = competitionTypeRepository.findById(competitionId)
            .orElseThrow { IllegalArgumentException("Invalid CompetitionType ID") }

        nomination.event = event
        nomination.nominationType = nominationType
        nomination.competitionType = competitionType
        return nominationRepository.save(nomination)
    }

    fun updateNomination(id: Int, nominationDetails: Nomination, eventId: Int, typeId: Int, competitionId: Int): Optional<Nomination> {
        return nominationRepository.findById(id).map { existingNomination ->
            val event = eventRepository.findById(eventId)
                .orElseThrow { IllegalArgumentException("Invalid Event ID") }
            val nominationType = nominationTypeRepository.findById(typeId)
                .orElseThrow { IllegalArgumentException("Invalid NominationType ID") }
            val competitionType = competitionTypeRepository.findById(competitionId)
                .orElseThrow { IllegalArgumentException("Invalid CompetitionType ID") }

            existingNomination.title = nominationDetails.title
            existingNomination.event = event
            existingNomination.nominationType = nominationType
            existingNomination.competitionType = competitionType
            nominationRepository.save(existingNomination)
        }
    }

    fun deleteNomination(id: Int): Boolean {
        if (nominationRepository.existsById(id)) {
            nominationRepository.deleteById(id)
            return true
        }
        return false
    }

    fun findNominationsByEventId(eventId: Int): List<Nomination> {
        return nominationRepository.findByEventId(eventId)
    }

    fun findNominationsByNominationTypeId(typeId: Int): List<Nomination> {
        return nominationRepository.findByNominationTypeId(typeId)
    }

    fun findNominationsByCompetitionTypeId(competitionTypeId: Int): List<Nomination> {
        return nominationRepository.findByCompetitionTypeId(competitionTypeId)
    }
}
