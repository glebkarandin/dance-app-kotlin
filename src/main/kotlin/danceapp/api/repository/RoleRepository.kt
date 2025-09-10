package danceapp.api.repository

import danceapp.api.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RoleRepository : JpaRepository<Role, Int> {
    fun findByCode(code: String): Optional<Role>
}
