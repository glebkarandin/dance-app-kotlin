package danceapp.api.repository

import danceapp.api.model.Result
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ResultRepository : JpaRepository<Result, Int> {

    @Query("SELECT r FROM Result r " +
           "JOIN FETCH r.nomination n " +
           "JOIN FETCH r.participant " +
           "JOIN FETCH n.event " +
           "JOIN FETCH n.nominationType " +
           "JOIN FETCH n.competitionType")
    fun findAllWithDetails(): List<Result>

    fun findByNominationId(nominationId: Int): List<Result>
    fun findByParticipantId(participantId: Int): List<Result>
    fun findByNominationIdAndParticipantId(nominationId: Int, participantId: Int): List<Result>
}
