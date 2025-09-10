package danceapp.api.repository

import danceapp.api.model.Nomination
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface NominationRepository : JpaRepository<Nomination, Int> {

    @Query("SELECT n FROM Nomination n " +
           "JOIN FETCH n.event " +
           "JOIN FETCH n.nominationType " +
           "JOIN FETCH n.competitionType")
    fun findAllWithDetails(): List<Nomination>

    fun findByEventId(eventId: Int): List<Nomination>
    fun findByNominationTypeId(typeId: Int): List<Nomination>
    fun findByCompetitionTypeId(competitionTypeId: Int): List<Nomination>
}
