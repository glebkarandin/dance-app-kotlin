package danceapp.api.service

import danceapp.api.dto.AuthResponseDto
import danceapp.api.model.AppUser
import danceapp.api.model.Role
import danceapp.api.model.TelegramProfile
import danceapp.api.repository.AppUserRepository
import danceapp.api.repository.RoleRepository
import danceapp.api.repository.TelegramProfileRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

class TelegramProfileServiceTest {

    @Mock
    private lateinit var telegramProfileRepository: TelegramProfileRepository

    @Mock
    private lateinit var appUserRepository: AppUserRepository

    @Mock
    private lateinit var roleRepository: RoleRepository

    @InjectMocks
    private lateinit var telegramProfileService: TelegramProfileService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `findOrCreateUserByTelegramProfile for existing profile`() {
        val profile = TelegramProfile().apply {
            telegramId = 123
            user = AppUser().apply {
                id = 1
                roles = emptySet()
            }
        }

        `when`(telegramProfileRepository.findByTelegramId(123)).thenReturn(Optional.of(profile))

        val result = telegramProfileService.findOrCreateUserByTelegramProfile(profile)

        assertNotNull(result)
        assertEquals(1, result.id)
        verify(telegramProfileRepository, times(1)).findByTelegramId(123)
        verify(appUserRepository, never()).save(any(AppUser::class.java))
        verify(telegramProfileRepository, never()).save(any(TelegramProfile::class.java))
    }

    @Test
    fun `findOrCreateUserByTelegramProfile for new profile`() {
        val profile = TelegramProfile().apply {
            telegramId = 123
            firstName = "Test"
            lastName = "User"
            userName = "testuser"
        }

        val role = Role().apply {
            code = "REGISTERED_USER"
            description = "Registered User" // This was missing
            permissions = emptySet()
        }

        `when`(telegramProfileRepository.findByTelegramId(123)).thenReturn(Optional.empty())
        `when`(roleRepository.findByCode("REGISTERED_USER")).thenReturn(Optional.of(role))
        `when`(appUserRepository.save(any(AppUser::class.java))).thenAnswer {
            val user = it.getArgument<AppUser>(0)
            user.id = 1
            user.roles = setOf(role)
            user
        }

        val result = telegramProfileService.findOrCreateUserByTelegramProfile(profile)

        assertNotNull(result)
        assertEquals(1, result.id)
        assertFalse(result.roles.isEmpty())
        assertEquals("REGISTERED_USER", result.roles.iterator().next().code)
        verify(telegramProfileRepository, times(1)).findByTelegramId(123)
        verify(appUserRepository, times(1)).save(any(AppUser::class.java))
        verify(telegramProfileRepository, times(1)).save(any(TelegramProfile::class.java))
    }
}
