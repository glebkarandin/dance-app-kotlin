package danceapp.api.repository

import danceapp.api.model.Permission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PermissionRepository : JpaRepository<Permission, Int> {
    fun findByCode(code: String): Optional<Permission>
}
