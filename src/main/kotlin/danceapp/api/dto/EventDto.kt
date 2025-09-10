package danceapp.api.dto

import danceapp.api.model.Event
import java.time.OffsetDateTime

data class EventDto(
    val id: Int?,
    val title: String,
    val description: String?,
    val dateStart: OffsetDateTime,
    val dateEnd: OffsetDateTime,
    val city: String?,
    val country: String?
) {
    fun toEntity(): Event {
        val entity = Event()
        entity.id = this.id
        entity.title = this.title
        entity.description = this.description
        entity.dateStart = this.dateStart
        entity.dateEnd = this.dateEnd
        entity.city = this.city
        entity.country = this.country
        return entity
    }

    companion object {
        fun fromEntity(entity: Event?): EventDto? {
            if (entity == null) return null
            return EventDto(
                entity.id,
                entity.title,
                entity.description,
                entity.dateStart,
                entity.dateEnd,
                entity.city,
                entity.country
            )
        }
    }
}
