package danceapp.api.repository

import danceapp.api.model.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.time.OffsetDateTime
import java.time.ZoneOffset
import org.assertj.core.api.Assertions.assertThat

@DataJpaTest
@ActiveProfiles("test")
class ParticipantRepositoryIntegrationTest @Autowired constructor(
    private val participantRepository: ParticipantRepository,
    private val eventRepository: EventRepository,
    private val nominationRepository: NominationRepository,
    private val resultRepository: ResultRepository,
    private val competitionTypeRepository: CompetitionTypeRepository,
    private val nominationTypeRepository: NominationTypeRepository
) {

    private lateinit var participant1: Participant
    private lateinit var participant2: Participant
    private lateinit var event1: Event
    private lateinit var event2: Event
    private lateinit var event3: Event

    @BeforeEach
    fun setUp() {
        participant1 = Participant().apply { firstName = "John"; lastName = "Doe" }
        participantRepository.save(participant1)

        participant2 = Participant().apply { firstName = "Jane"; lastName = "Smith" }
        participantRepository.save(participant2)

        event1 = Event().apply {
            title = "Event 2024"
            dateStart = OffsetDateTime.of(2024, 10, 15, 10, 0, 0, 0, ZoneOffset.UTC)
            dateEnd = OffsetDateTime.of(2024, 10, 16, 18, 0, 0, 0, ZoneOffset.UTC)
        }
        eventRepository.save(event1)

        event2 = Event().apply {
            title = "Event 2025"
            dateStart = OffsetDateTime.of(2025, 11, 20, 10, 0, 0, 0, ZoneOffset.UTC)
            dateEnd = OffsetDateTime.of(2025, 11, 21, 18, 0, 0, 0, ZoneOffset.UTC)
        }
        eventRepository.save(event2)

        event3 = Event().apply {
            title = "Event 2023"
            dateStart = OffsetDateTime.of(2023, 12, 1, 10, 0, 0, 0, ZoneOffset.UTC)
            dateEnd = OffsetDateTime.of(2023, 12, 2, 18, 0, 0, 0, ZoneOffset.UTC)
        }
        eventRepository.save(event3)

        val compType = CompetitionType().apply { code = "SOLO" }
        competitionTypeRepository.save(compType)

        val nomType = NominationType().apply { code = "CONTEMPORARY" }
        nominationTypeRepository.save(nomType)

        val nomination1 = Nomination().apply {
            title = "Nomination 1"
            event = event1
            competitionType = compType
            nominationType = nomType
        }
        nominationRepository.save(nomination1)

        val nomination2 = Nomination().apply {
            title = "Nomination 2"
            event = event2
            competitionType = compType
            nominationType = nomType
        }
        nominationRepository.save(nomination2)

        Result().apply {
            participant = participant1
            nomination = nomination1
            place = 1
        }.also { resultRepository.save(it) }

        Result().apply {
            participant = participant2
            nomination = nomination2
            place = 2
        }.also { resultRepository.save(it) }
    }

    @AfterEach
    fun tearDown() {
        resultRepository.deleteAll()
        nominationRepository.deleteAll()
        eventRepository.deleteAll()
        participantRepository.deleteAll()
        competitionTypeRepository.deleteAll()
        nominationTypeRepository.deleteAll()
    }

    @Test
    fun `when find participants by event date range then returns correct participants`() {
        val startDate2024 = OffsetDateTime.of(2024, 9, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        val endDate2025 = OffsetDateTime.of(2025, 8, 31, 23, 59, 59, 0, ZoneOffset.UTC)
        val participants2024 = participantRepository.findParticipantsByEventDateRange(startDate2024, endDate2025)
        assertThat(participants2024).hasSize(1)
        assertThat(participants2024[0].firstName).isEqualTo("John")

        val startDate2025 = OffsetDateTime.of(2025, 9, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        val endDate2026 = OffsetDateTime.of(2026, 8, 31, 23, 59, 59, 0, ZoneOffset.UTC)
        val participants2025 = participantRepository.findParticipantsByEventDateRange(startDate2025, endDate2026)
        assertThat(participants2025).hasSize(1)
        assertThat(participants2025[0].firstName).isEqualTo("Jane")
    }

    @Test
    fun `when no participants in date range then returns empty list`() {
        val startDate = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        val endDate = OffsetDateTime.of(2020, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC)
        val participants = participantRepository.findParticipantsByEventDateRange(startDate, endDate)
        assertThat(participants).isEmpty()
    }
}
