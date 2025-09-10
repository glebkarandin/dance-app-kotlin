package danceapp.api.controller

import danceapp.api.dto.AppUserDto
import danceapp.api.model.AppUser
import danceapp.api.service.AppUserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/bknd/api/users")
class AppUserController(private val appUserService: AppUserService) {

    @GetMapping
    fun getAllUsers(): List<AppUserDto?> {
        return appUserService.getAllUsers().stream()
            .map(AppUserDto::fromEntity)
            .collect(Collectors.toList())
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Int): ResponseEntity<AppUserDto> {
        return appUserService.getUserById(id)
            .map { user -> AppUserDto.fromEntity(user) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/by-telegram-id/{telegramId}")
    fun getUserByTelegramId(@PathVariable telegramId: Int): ResponseEntity<AppUserDto> {
        return appUserService.getUserByTelegramId(telegramId)
            .map { user -> AppUserDto.fromEntity(user) }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createUser(): ResponseEntity<AppUserDto> {
        val newUser = AppUser()
        val createdUser = appUserService.createUser(newUser)
        return ResponseEntity.status(HttpStatus.CREATED).body(AppUserDto.fromEntity(createdUser))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Int): ResponseEntity<Void> {
        return if (appUserService.deleteUser(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
