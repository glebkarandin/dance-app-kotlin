package danceapp.api.service

import danceapp.api.dto.AuthResponseDto
import danceapp.api.model.AppUser
import danceapp.api.model.TelegramProfile
import danceapp.api.repository.AppUserRepository
import danceapp.api.repository.RoleRepository
import danceapp.api.repository.TelegramProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TelegramProfileService(
    private val telegramProfileRepository: TelegramProfileRepository,
    private val appUserRepository: AppUserRepository,
    private val roleRepository: RoleRepository
) {

    fun getAllTelegramProfiles(): List<TelegramProfile> {
        return telegramProfileRepository.findAll()
    }

    fun getTelegramProfileById(id: Int): Optional<TelegramProfile> {
        return telegramProfileRepository.findById(id)
    }

    fun getTelegramProfileByUserId(userId: Int): Optional<TelegramProfile> {
        return telegramProfileRepository.findByUserId(userId)
    }

    fun getTelegramProfileByTelegramId(telegramId: Int): Optional<TelegramProfile> {
        return telegramProfileRepository.findByTelegramId(telegramId)
    }

    fun getTelegramProfileByUserName(userName: String): Optional<TelegramProfile> {
        return telegramProfileRepository.findByUserName(userName)
    }

    fun createTelegramProfile(telegramProfile: TelegramProfile, userId: Int): TelegramProfile {
        val appUser = appUserRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("Invalid AppUser ID: $userId") }
        telegramProfile.user = appUser
        return telegramProfileRepository.save(telegramProfile)
    }

    fun findOrCreateUserByTelegramProfile(telegramProfile: TelegramProfile): AuthResponseDto {
        val existingProfileOpt = telegramProfileRepository.findByTelegramId(telegramProfile.telegramId)

        return if (existingProfileOpt.isPresent) {
            val user = existingProfileOpt.get().user
            AuthResponseDto.fromEntity(user)!!
        } else {
            val newUser = AppUser()
            val role = roleRepository.findByCode("REGISTERED_USER")
                .orElseThrow { IllegalStateException("Role with code REGISTERED_USER not found") }
            newUser.roles = Collections.singleton(role)
            val savedUser = appUserRepository.save(newUser)

            telegramProfile.user = savedUser
            telegramProfileRepository.save(telegramProfile)

            AuthResponseDto.fromEntity(savedUser)!!
        }
    }

    fun updateTelegramProfile(profileId: Int, profileDetails: TelegramProfile): Optional<TelegramProfile> {
        return telegramProfileRepository.findById(profileId)
            .map { existingProfile ->
                existingProfile.firstName = profileDetails.firstName
                existingProfile.lastName = profileDetails.lastName
                existingProfile.userName = profileDetails.userName
                existingProfile.telegramId = profileDetails.telegramId
                // user_id typically not changed here.
                telegramProfileRepository.save(existingProfile)
            }
    }

    fun deleteTelegramProfile(id: Int): Boolean {
        if (telegramProfileRepository.existsById(id)) {
            telegramProfileRepository.deleteById(id)
            return true
        }
        return false
    }
}
