package es.unex.giss.asee.ghiblitrunk.data.models
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Comment(
    @PrimaryKey(autoGenerate = true) val reviewId: Long?,
    val newsId: Long,
    val userId: Long,
    val content: String,
): Serializable