package es.unex.giss.asee.ghiblitrunk.data.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_character_cross_ref",
    primaryKeys = ["userId", "characterId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Character::class,
            parentColumns = ["id"],
            childColumns = ["characterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserCharacterCrossRef(
    val userId: Long,
    val characterId: String
)