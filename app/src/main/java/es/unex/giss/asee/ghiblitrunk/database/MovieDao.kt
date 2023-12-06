package es.unex.giss.asee.ghiblitrunk.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.data.models.UserMovieCrossRef
import es.unex.giss.asee.ghiblitrunk.data.models.UserWithMovies

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserMovie(crossRef: UserMovieCrossRef)

    @Delete
    suspend fun delete(movie: Movie)

    @Delete
    suspend fun delete(userMovieCrossRef: UserMovieCrossRef)

    @Query("SELECT count(*) FROM movies")
    suspend fun getNumberOfMovies(): Long

    @Query("SELECT is_favourite FROM movies Where id = :id")
    suspend fun getIfFavorite(id: String): Boolean

    @Query("SELECT * FROM movies Where is_favourite = 1")
    suspend fun getFavorites(): List<Movie>

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: String): Movie

    @Update
    suspend fun update(movie: Movie)

    @Transaction
    @Query("SELECT * FROM User where userId = :userId")
    fun getUserWithMovies(userId: Long): LiveData<UserWithMovies>
}