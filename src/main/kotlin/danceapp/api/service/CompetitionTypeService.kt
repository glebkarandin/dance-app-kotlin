package danceapp.api.service

import danceapp.api.model.CompetitionType
import danceapp.api.repository.CompetitionTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
class CompetitionTypeService(private val competitionTypeRepository: CompetitionTypeRepository) {

    fun getAllCompetitionTypes(): List<CompetitionType> {
        return competitionTypeRepository.findAll()
    }

    fun getCompetitionTypeById(id: Int): Optional<CompetitionType> {
        return competitionTypeRepository.findById(id)
    }

    fun getCompetitionTypeByCode(code: String): Optional<CompetitionType> {
        return competitionTypeRepository.findByCode(code)
    }

    fun createCompetitionType(competitionType: CompetitionType): CompetitionType {
        return competitionTypeRepository.save(competitionType)
    }

    fun updateCompetitionType(id: Int, competitionTypeDetails: CompetitionType): Optional<CompetitionType> {
        return competitionTypeRepository.findById(id)
            .map { existingType ->
                existingType.code = competitionTypeDetails.code
                existingType.description = competitionTypeDetails.description
                competitionTypeRepository.save(existingType)
            }
    }

    fun deleteCompetitionType(id: Int): Boolean {
        if (competitionTypeRepository.existsById(id)) {
            // Consider if there are Nominations using this type.
            // Deletion might need to be restricted or cascaded.
            competitionTypeRepository.deleteById(id)
            return true
        }
        return false
    }
}
