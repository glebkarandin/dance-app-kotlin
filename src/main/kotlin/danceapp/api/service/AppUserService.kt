package danceapp.api.service

import danceapp.api.model.AppUser
import danceapp.api.repository.AppUserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
class AppUserService(private val appUserRepository: AppUserRepository) {

    fun getAllUsers(): List<AppUser> {
        return appUserRepository.findAllWithRoles()
    }

    fun getUserById(id: Int): Optional<AppUser> {
        return appUserRepository.findByIdWithRoles(id)
    }

    fun getUserByTelegramId(telegramId: Int): Optional<AppUser> {
        return appUserRepository.findByTelegramId(telegramId)
    }

    fun createUser(appUser: AppUser): AppUser {
        // Add any business logic before saving, e.g., validation, setting defaults
        return appUserRepository.save(appUser)
    }

    fun updateUser(id: Int, appUserDetails: AppUser): Optional<AppUser> {
        return appUserRepository.findById(id)
            .map { existingUser ->
                // Update allowed fields
                // existingUser.roles = appUserDetails.roles // Example, handle carefully
                appUserRepository.save(existingUser)
            }
    }

    fun deleteUser(id: Int): Boolean {
        if (appUserRepository.existsById(id)) {
            appUserRepository.deleteById(id)
            return true
        }
        return false
    }
}
