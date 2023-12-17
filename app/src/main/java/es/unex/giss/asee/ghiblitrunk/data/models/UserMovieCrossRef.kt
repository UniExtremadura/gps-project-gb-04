package es.unex.giss.asee.ghiblitrunk.data.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_movie_cross_ref",
    primaryKeys = ["userId", "movieId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserMovieCrossRef(
    val userId: Long,
    val movieId: String
)
