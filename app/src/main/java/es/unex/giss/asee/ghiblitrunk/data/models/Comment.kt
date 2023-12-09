package es.unex.giss.asee.ghiblitrunk.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = UserMovieCrossRef::class,
            parentColumns = ["userId", "movieId"],
            childColumns = ["userId", "movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Comment(
    @PrimaryKey(autoGenerate = true) val commentId: Long = 0,
    val userId: Long, // ID del usuario que hizo el comentario
    val movieId: String, // ID de la película a la que pertenece el comentario
    val text: String, // Contenido del comentario
    val timestamp: Date = Date() // Marca de tiempo del comentario
)
