package danceapp.api.service

import danceapp.api.model.NominationType
import danceapp.api.repository.NominationTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
class NominationTypeService(private val nominationTypeRepository: NominationTypeRepository) {

    fun getAllNominationTypes(): List<NominationType> {
        return nominationTypeRepository.findAll()
    }

    fun getNominationTypeById(id: Int): Optional<NominationType> {
        return nominationTypeRepository.findById(id)
    }

    fun getNominationTypeByCode(code: String): Optional<NominationType> {
        return nominationTypeRepository.findByCode(code)
    }

    fun createNominationType(nominationType: NominationType): NominationType {
        return nominationTypeRepository.save(nominationType)
    }

    fun updateNominationType(id: Int, nominationTypeDetails: NominationType): Optional<NominationType> {
        return nominationTypeRepository.findById(id)
            .map { existingType ->
                existingType.code = nominationTypeDetails.code
                existingType.description = nominationTypeDetails.description
                nominationTypeRepository.save(existingType)
            }
    }

    fun deleteNominationType(id: Int): Boolean {
        if (nominationTypeRepository.existsById(id)) {
            // Similar considerations for Nominations using this type.
            nominationTypeRepository.deleteById(id)
            return true
        }
        return false
    }
}
