package danceapp.api.controller

import danceapp.api.dto.UserProfileCreateUpdateDto
import danceapp.api.dto.UserProfileDto
import danceapp.api.service.UserProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bknd/api/user-profiles")
class UserProfileController(private val userProfileService: UserProfileService) {

    @GetMapping
    fun getAllUserProfiles(): List<UserProfileDto?> {
        return userProfileService.getAllUserProfiles().map(UserProfileDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getUserProfileById(@PathVariable id: Int): ResponseEntity<UserProfileDto> {
        return userProfileService.getUserProfileById(id)
            .map { profile -> UserProfileDto.fromEntity(profile) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/user/{userId}")
    fun getUserProfileByUserId(@PathVariable userId: Int): ResponseEntity<UserProfileDto> {
        return userProfileService.getUserProfileByUserId(userId)
            .map { profile -> UserProfileDto.fromEntity(profile) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/email/{email}")
    fun getUserProfileByEmail(@PathVariable email: String): ResponseEntity<UserProfileDto> {
        return userProfileService.getUserProfileByEmail(email)
            .map { profile -> UserProfileDto.fromEntity(profile) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createUserProfile(@RequestBody dto: UserProfileCreateUpdateDto): ResponseEntity<UserProfileDto> {
        return try {
            val profile = dto.toEntity()
            val createdProfile = userProfileService.createUserProfile(profile, dto.userId)
            ResponseEntity.status(HttpStatus.CREATED).body(UserProfileDto.fromEntity(createdProfile))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun updateUserProfile(@PathVariable id: Int, @RequestBody dto: UserProfileCreateUpdateDto): ResponseEntity<UserProfileDto> {
        val profileDetails = dto.toEntity()
        return userProfileService.updateUserProfile(id, profileDetails)
            .map { profile -> UserProfileDto.fromEntity(profile) }
            .map { updatedDto -> ResponseEntity.ok(updatedDto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteUserProfile(@PathVariable id: Int): ResponseEntity<Void> {
        return if (userProfileService.deleteUserProfile(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
