package es.unex.giss.asee.ghiblitrunk.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.unex.giss.asee.ghiblitrunk.data.models.Comment

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReview(comment: Comment)

    @Query("SELECT * FROM Comment WHERE userId = :userId AND newsId = :newsId")
    suspend fun getUserReviews(userId: Long, newsId: Long): List<Comment>
}
