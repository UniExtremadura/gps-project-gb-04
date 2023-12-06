package es.unex.giss.asee.ghiblitrunk.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import es.unex.giss.asee.ghiblitrunk.api.ApiService
import es.unex.giss.asee.ghiblitrunk.api.RetrofitClient
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.database.CharacterDao
import es.unex.giss.asee.ghiblitrunk.database.MovieDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository private constructor(
    private val characterDao: CharacterDao,
    private val moviesDao: MovieDao,
    private val networkService: ApiService
) {
    private var lastUpdateTimeMillis: Long = 0L
    val characters = characterDao.getAllPeople()
    val movies = moviesDao.getAllMovies()

    private val userFilter = MutableLiveData<Long>()

    fun setUserId(userId: Long){
        userFilter.value = userId
    }

    suspend fun tryUpdateRecentDataCache()
    {
        if (shouldUpdateMoviesCache())
            fetchRecentMovies()

        if (shouldUpdateCharactersCache())
            fetchRecentCharacters()
    }

    //region Library

    suspend fun movieToLibrary(movie: Movie, userId: Long){
        movie.isFavourite = true
        moviesDao.update(movie)
    }

    suspend fun characterToLibrary(character: Character, userId: Long){
        character.isFavourite = true
        characterDao.update(character)
    }

    suspend fun deleteMovieFromLibrary(movie: Movie, userId: Long){
        movie.isFavourite = false
        moviesDao.update(movie)
    }

    suspend fun deleteCharacterFromLibrary(character: Character, userId: Long){
        character.isFavourite = false
        characterDao.update(character)
    }

    suspend fun getIfFavorite(movie: Movie): Boolean{
        return moviesDao.getIfFavorite(movie.id)
    }

    suspend fun getIfFavorite(character: Character): Boolean{
        return characterDao.getIfFavorite(character.id)
    }

    suspend fun getFavoritesCharacters(): List<Character>{
        return characterDao.getFavorites()
    }

    suspend fun getFavoritesMovies(): List<Movie>{
        return moviesDao.getFavorites()
    }

    //endregion

    //region Data Fetch

    private suspend fun fetchRecentCharacters()
    {
        try {
            val response = RetrofitClient.apiService.getAllPeople()
            if (response.isSuccessful) {
                val charactersResponse = response.body()
                charactersResponse?.let {
                    // Ejecutar la inserción de la base de datos en un contexto adecuado
                    CoroutineScope(Dispatchers.IO).launch {
                        characterDao.insertAll(it)
                        lastUpdateTimeMillis = System.currentTimeMillis()
                    }
                }
            } else {
                Log.e("REPOSITORY_CHARACTERS", "API Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("REPOSITORY_CHARACTERS", "Error on API call: ${e.message}")
        }
    }

    suspend fun fetchCharacterDetail(characterId: String): Character?{
        val response = try {
            RetrofitClient.apiService.getPersonById(characterId)
        } catch (e: Exception) {
            Log.e("REPOSITORY_CHARACTER_DETAIL", "Error on API call: ${e.message}")
            return null
        }

        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.e("REPOSITORY_CHARACTER_DETAIL", "API Error: ${response.errorBody()?.string()}")
            null
        }
    }

    suspend fun fetchMovieDetail(movieId: String): Movie?{
        val response = try {
            RetrofitClient.apiService.getFilmById(movieId)
        } catch (e: Exception) {
            Log.e("REPOSITORY_MOVIE_DETAIL", "Error on API call: ${e.message}")
            return null
        }

        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.e("REPOSITORY_MOVIE_DETAIL", "API Error: ${response.errorBody()?.string()}")
            null
        }
    }

    private suspend fun fetchRecentMovies() {
        try {
            val response = RetrofitClient.apiService.getAllFilms()
            if (response.isSuccessful) {
                val moviesResponse = response.body()
                moviesResponse?.let {
                    // Ejecutar la inserción de la base de datos en un contexto adecuado
                    CoroutineScope(Dispatchers.IO).launch {
                        moviesDao.insertAll(it)
                        lastUpdateTimeMillis = System.currentTimeMillis()
                    }
                }
            } else {
                Log.e("REPOSITORY_MOVIES", "API Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("REPOSITORY_MOVIES", "Error on API call: ${e.message}")
        }
    }

    //endregion


    private suspend fun shouldUpdateMoviesCache(): Boolean
    {
        val lastFetchTimeMillis = lastUpdateTimeMillis
        val timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS || moviesDao.getNumberOfMovies() == 0L
    }

    private suspend fun shouldUpdateCharactersCache(): Boolean
    {
        val lastFetchTimeMillis = lastUpdateTimeMillis
        val timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS || characterDao.getNumberOfCharacters() == 0L
    }

    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000

        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(
            characterDao: CharacterDao,
            moviesDao: MovieDao,
            apiService: ApiService
        ): Repository
        {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(characterDao, moviesDao, apiService).also { INSTANCE = it }
            }
        }
    }
}