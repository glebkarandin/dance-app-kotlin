package danceapp.api.service

import danceapp.api.model.VotesTheme
import danceapp.api.repository.VotesThemeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
class VotesThemeService(private val votesThemeRepository: VotesThemeRepository) {

    fun getAllVotesThemes(): List<VotesTheme> {
        return votesThemeRepository.findAll()
    }

    fun getVotesThemeById(id: Int): Optional<VotesTheme> {
        return votesThemeRepository.findById(id)
    }

    fun getVotesThemeByThemeCode(themeCode: String): Optional<VotesTheme> {
        return votesThemeRepository.findByThemeCode(themeCode)
    }

    fun createVotesTheme(votesTheme: VotesTheme): VotesTheme {
        return votesThemeRepository.save(votesTheme)
    }

    fun updateVotesTheme(id: Int, votesThemeDetails: VotesTheme): Optional<VotesTheme> {
        return votesThemeRepository.findById(id)
            .map { existingTheme ->
                existingTheme.themeCode = votesThemeDetails.themeCode
                existingTheme.description = votesThemeDetails.description
                existingTheme.periodType = votesThemeDetails.periodType
                votesThemeRepository.save(existingTheme)
            }
    }

    fun deleteVotesTheme(id: Int): Boolean {
        if (votesThemeRepository.existsById(id)) {
            // Consider implications for VoteLikes using this theme.
            votesThemeRepository.deleteById(id)
            return true
        }
        return false
    }
}
