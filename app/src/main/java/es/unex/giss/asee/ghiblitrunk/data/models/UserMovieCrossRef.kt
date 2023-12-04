package es.unex.giss.asee.ghiblitrunk.data.models

import androidx.room.Entity
import androidx.room.ForeignKey

// TODO: Modificar valores de los id's
@Entity(
    primaryKeys = ["userId", "newsId"],
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["newsId"],
            childColumns = ["newsId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserMovieCrossRef(
    val userId: Long,
    val newsId: Long
)
