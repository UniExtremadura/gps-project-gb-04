package es.unex.giss.asee.ghiblitrunk.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.unex.giss.asee.ghiblitrunk.data.models.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: String): Movie
}