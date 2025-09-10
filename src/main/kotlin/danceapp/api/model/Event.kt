package danceapp.api.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "event")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(nullable = false)
    lateinit var title: String

    var description: String? = null

    @Column(name = "date_start", nullable = false)
    lateinit var dateStart: OffsetDateTime

    @Column(name = "date_end", nullable = false)
    lateinit var dateEnd: OffsetDateTime

    var city: String? = null

    var country: String? = null

    @OneToMany(mappedBy = "event")
    var nominations: Set<Nomination> = HashSet()

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val event = other as Event
        return id != null && id == event.id
    }
}
