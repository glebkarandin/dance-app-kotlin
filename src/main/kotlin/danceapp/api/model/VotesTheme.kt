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

@Entity
@Table(name = "votes_theme")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
class VotesTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "theme_code", nullable = false, unique = true)
    lateinit var themeCode: String

    @Column(nullable = false)
    lateinit var description: String

    @Column(name = "period_type", nullable = false)
    var periodType: String = "WEEK"

    @OneToMany(mappedBy = "votesTheme")
    var voteLikes: Set<VoteLike> = HashSet()

    override fun hashCode(): Int {
        return id ?: themeCode.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VotesTheme

        if (id != null && id == other.id) return true
        return themeCode == other.themeCode
    }
}
