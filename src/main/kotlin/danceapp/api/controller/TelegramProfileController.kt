package danceapp.api.controller

import danceapp.api.dto.AuthResponseDto
import danceapp.api.dto.TelegramProfileCreateUpdateDto
import danceapp.api.dto.TelegramProfileDto
import danceapp.api.service.TelegramProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bknd/api/telegram-profiles")
class TelegramProfileController(private val telegramProfileService: TelegramProfileService) {

    @GetMapping
    fun getAllTelegramProfiles(): List<TelegramProfileDto?> {
        return telegramProfileService.getAllTelegramProfiles().map(TelegramProfileDto::fromEntity)
    }

    @GetMapping("/{id}")
    fun getTelegramProfileById(@PathVariable id: Int): ResponseEntity<TelegramProfileDto> {
        return telegramProfileService.getTelegramProfileById(id)
            .map { profile -> TelegramProfileDto.fromEntity(profile) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/user/{userId}")
    fun getTelegramProfileByUserId(@PathVariable userId: Int): ResponseEntity<TelegramProfileDto> {
        return telegramProfileService.getTelegramProfileByUserId(userId)
            .map { profile -> TelegramProfileDto.fromEntity(profile) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/telegramId/{telegramId}")
    fun getTelegramProfileByTelegramId(@PathVariable telegramId: Int): ResponseEntity<TelegramProfileDto> {
        return telegramProfileService.getTelegramProfileByTelegramId(telegramId)
            .map { profile -> TelegramProfileDto.fromEntity(profile) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/userName/{userName}")
    fun getTelegramProfileByUserName(@PathVariable userName: String): ResponseEntity<TelegramProfileDto> {
        return telegramProfileService.getTelegramProfileByUserName(userName)
            .map { profile -> TelegramProfileDto.fromEntity(profile) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createTelegramProfile(@RequestBody dto: TelegramProfileCreateUpdateDto): ResponseEntity<TelegramProfileDto> {
        return try {
            val profile = dto.toEntity()
            val createdProfile = telegramProfileService.createTelegramProfile(profile, dto.userId)
            ResponseEntity.status(HttpStatus.CREATED).body(TelegramProfileDto.fromEntity(createdProfile))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/auth")
    fun auth(@RequestBody dto: TelegramProfileCreateUpdateDto): ResponseEntity<AuthResponseDto> {
        val profile = dto.toEntity()
        val authResponse = telegramProfileService.findOrCreateUserByTelegramProfile(profile)
        return ResponseEntity.ok(authResponse)
    }

    @PutMapping("/{id}")
    fun updateTelegramProfile(@PathVariable id: Int, @RequestBody dto: TelegramProfileCreateUpdateDto): ResponseEntity<TelegramProfileDto> {
        val profileDetails = dto.toEntity()
        return telegramProfileService.updateTelegramProfile(id, profileDetails)
            .map { profile -> TelegramProfileDto.fromEntity(profile) }
            .map { updatedDto -> ResponseEntity.ok(updatedDto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteTelegramProfile(@PathVariable id: Int): ResponseEntity<Void> {
        return if (telegramProfileService.deleteTelegramProfile(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
