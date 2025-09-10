package danceapp.api.controller

import danceapp.api.model.AppUser
import danceapp.api.model.Permission
import danceapp.api.model.Role
import danceapp.api.model.TelegramProfile
import danceapp.api.repository.AppUserRepository
import danceapp.api.repository.PermissionRepository
import danceapp.api.repository.RoleRepository
import danceapp.api.repository.TelegramProfileRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class AppUserControllerIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val appUserRepository: AppUserRepository,
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository,
    private val telegramProfileRepository: TelegramProfileRepository
) {

    @Test
    fun `getAllUsers should return users with roles and permissions`() {
        // Arrange
        val readPermission = Permission().apply {
            code = "USER_READ"
            description = "Read user data"
        }
        permissionRepository.save(readPermission)

        val userRole = Role().apply {
            code = "ROLE_USER"
            description = "Standard user role"
            permissions = setOf(readPermission)
        }
        roleRepository.save(userRole)

        val user = AppUser().apply {
            roles = setOf(userRole)
        }
        appUserRepository.save(user)

        // Act & Assert
        mockMvc.get("/bknd/api/users") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.length()") { value(1) }
            jsonPath("$[0].id") { value(user.id!!) }
            jsonPath("$[0].roles.length()") { value(1) }
            jsonPath("$[0].roles[0].code") { value("ROLE_USER") }
            jsonPath("$[0].roles[0].permissions.length()") { value(1) }
            jsonPath("$[0].roles[0].permissions[0].code") { value("USER_READ") }
        }
    }

    @Test
    fun `getUserByTelegramId should return user`() {
        // Arrange
        val user = AppUser()
        appUserRepository.save(user)

        val profile = TelegramProfile().apply {
            telegramId = 12345
            this.user = user
            firstName = "Test"
            lastName = "User"
            userName = "testuser"
        }
        telegramProfileRepository.save(profile)

        // Act & Assert
        mockMvc.get("/bknd/api/users/by-telegram-id/12345") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(user.id!!) }
        }
    }
}
