package es.unex.giss.asee.ghiblitrunk.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.unex.giss.asee.ghiblitrunk.data.models.Comment

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertComment(comment: Comment)

    @Query("SELECT * FROM comments WHERE movieId = :movieId")
    suspend fun getCommentsForMovie(movieId: String): List<Comment>

    @Query("DELETE FROM comments WHERE commentId = :commentId")
    suspend fun deleteComment(commentId: Long)
}
