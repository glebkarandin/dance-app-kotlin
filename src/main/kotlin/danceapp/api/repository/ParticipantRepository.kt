package danceapp.api.repository

import danceapp.api.model.Participant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
interface ParticipantRepository : JpaRepository<Participant, Int> {
    fun findByLastName(lastName: String): List<Participant>

    @Query("SELECT COUNT(DISTINCT p) FROM Participant p JOIN p.results r")
    fun countActiveParticipants(): Long

    @Query("SELECT COUNT(DISTINCT p) FROM Participant p JOIN p.results r JOIN r.nomination n JOIN n.event e WHERE e.dateStart >= :startDate AND e.dateStart <= :endDate")
    fun countActiveParticipantsInDateRange(@Param("startDate") startDate: OffsetDateTime, @Param("endDate") endDate: OffsetDateTime): Long

    @Query("SELECT DISTINCT p FROM Participant p JOIN FETCH p.results r JOIN FETCH r.nomination n JOIN FETCH n.event e WHERE e.dateStart >= :startDate AND e.dateStart <= :endDate")
    fun findParticipantsByEventDateRange(@Param("startDate") startDate: OffsetDateTime, @Param("endDate") endDate: OffsetDateTime): List<Participant>
}
