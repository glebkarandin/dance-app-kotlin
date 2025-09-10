package danceapp.api.service

import danceapp.api.model.UserProfile
import danceapp.api.repository.AppUserRepository
import danceapp.api.repository.UserProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserProfileService(
    private val userProfileRepository: UserProfileRepository,
    private val appUserRepository: AppUserRepository
) {

    fun getAllUserProfiles(): List<UserProfile> {
        return userProfileRepository.findAll()
    }

    fun getUserProfileById(id: Int): Optional<UserProfile> {
        return userProfileRepository.findById(id)
    }

    fun getUserProfileByUserId(userId: Int): Optional<UserProfile> {
        return userProfileRepository.findByUserId(userId)
    }

    fun getUserProfileByEmail(email: String): Optional<UserProfile> {
        return userProfileRepository.findByEmail(email)
    }

    fun createUserProfile(userProfile: UserProfile, userId: Int): UserProfile {
        val appUser = appUserRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("Invalid AppUser ID: $userId") }
        userProfile.user = appUser
        return userProfileRepository.save(userProfile)
    }

    fun updateUserProfile(profileId: Int, profileDetails: UserProfile): Optional<UserProfile> {
        return userProfileRepository.findById(profileId)
            .map { existingProfile ->
                existingProfile.firstName = profileDetails.firstName
                existingProfile.lastName = profileDetails.lastName
                existingProfile.email = profileDetails.email
                userProfileRepository.save(existingProfile)
            }
    }

    fun deleteUserProfile(id: Int): Boolean {
        if (userProfileRepository.existsById(id)) {
            userProfileRepository.deleteById(id)
            return true
        }
        return false
    }
}
