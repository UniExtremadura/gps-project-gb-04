package es.unex.giss.asee.ghiblitrunk.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.data.models.UserMovieCrossRef
import es.unex.giss.asee.ghiblitrunk.data.models.UserWithMovies

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun findMovieById(id: String): Movie

    @Query("SELECT * FROM movies WHERE url = :url")
    suspend fun findMovieByUrl(url: String): Movie

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(movies: List<Movie>)

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

    @Update
    suspend fun update(movie: Movie)

    @Transaction
    @Query("SELECT * FROM User where userId = :userId")
    fun getUserWithMovies(userId: Long): LiveData<UserWithMovies>

    // Relacionar un usuario a una noticia
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserMovie(crossRef: UserMovieCrossRef)

    @Transaction
    suspend fun insertAndRelate(movie: Movie, userId: Long) {
        val foundMovie = findMovieById(movie.id)
        if (foundMovie != null) {
            // La noticia ya existe, obtener el ID existente
            Log.e("MOVIE_DAO", "Movie already exists. ID: $foundMovie")
            foundMovie.id?.let { UserMovieCrossRef(userId, it) }?.let { insertUserMovie(it) }

        } else {
            // La noticia no existe, realizar la inserción
            insert(movie)
            Log.d("MOVIE_DAO", "Movie is being inserted. Its id is: $movie.id")
            insertUserMovie(UserMovieCrossRef(userId, movie.id))
        }

    }

    // region Barra de búsqueda
    @Query("SELECT * FROM movies WHERE title LIKE :title")
    fun searchMoviesByTitle(title: String): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE release_date LIKE :date")
    fun searchMoviesByDate(date: String): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE director LIKE :director")
    fun searchMoviesByDirector(director: String): LiveData<List<Movie>>

    // endregion
}