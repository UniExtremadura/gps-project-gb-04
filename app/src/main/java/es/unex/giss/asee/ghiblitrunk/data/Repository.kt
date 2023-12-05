package es.unex.giss.asee.ghiblitrunk.data

import android.util.Log
import es.unex.giss.asee.ghiblitrunk.api.ApiService
import es.unex.giss.asee.ghiblitrunk.api.RetrofitClient
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.database.CharacterDao
import es.unex.giss.asee.ghiblitrunk.database.MovieDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository private constructor(
    private val characterDao: CharacterDao,
    private val moviesDao: MovieDao,
    private val networkService: ApiService
) {
    private var lastUpdateTimeMillis: Long = 0L
    val characters = characterDao.getAllPeople()
    val movies = moviesDao.getAllMovies()

    suspend fun tryUpdateRecentDataCache()
    {
        if (shouldUpdateMoviesCache())
            fetchRecentMovies()

        if (shouldUpdateCharactersCache())
            fetchRecentCharacters()
    }

    private suspend fun fetchRecentCharacters()
    {
        val apiService: ApiService = RetrofitClient.apiService
        val call: Call<List<Character>> = apiService.getAllPeople()

        call.enqueue(object : Callback<List<Character>>
        {
            // Se ejecuta si recibimos respuesta
            override fun onResponse(call: Call<List<Character>>, response: Response<List<Character>>) {
                val result: Result<List<Character>> = RetrofitClient.handleApiResponse(response)

                // Si se produce error en la llamada se imprime
                if (!result.isSuccess) {
                    val error = result.exceptionOrNull()
                    Log.e("REPOSITORY_CHARACTERS", "Error: ${error?.message}")
                    return
                }

                Log.e("REPOSITORY_CHARACTERS", "CONEXIÓN A LA API REALIZADA")

                val charactersResponse = result.getOrNull()
                if (charactersResponse != null) {
                    // Ejecutar la inserción de la base de datos en un contexto adecuado
                    CoroutineScope(Dispatchers.IO).launch {
                        characterDao.insertAll(charactersResponse)
                        lastUpdateTimeMillis = System.currentTimeMillis()
                    }
                }
            }

            // Se ejecuta si se produce un error de red
            override fun onFailure(call: Call<List<Character>>, t: Throwable) {
                Log.e("REPOSITORY_CHARACTERS", "Error on API call: ${t.message}")
            }
        })
    }

    private suspend fun fetchRecentMovies()
    {
        val apiService: ApiService = RetrofitClient.apiService
        val call: Call<List<Movie>> = apiService.getAllFilms()

        call.enqueue(object : Callback<List<Movie>>
        {
            // Se ejecuta si recibimos respuesta
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                val result: Result<List<Movie>> = RetrofitClient.handleApiResponse(response)

                // Si se produce error en la llamada se imprime
                if (!result.isSuccess) {
                    val error = result.exceptionOrNull()
                    Log.e("REPOSITORY_MOVIES", "Error: ${error?.message}")
                    return
                }

                Log.e("REPOSITORY_MOVIES", "CONEXIÓN A LA API REALIZADA")

                val charactersResponse = result.getOrNull()
                if (charactersResponse != null) {
                    // Ejecutar la inserción de la base de datos en un contexto adecuado
                    CoroutineScope(Dispatchers.IO).launch {
                        moviesDao.insertAll(charactersResponse)
                        lastUpdateTimeMillis = System.currentTimeMillis()
                    }
                }
            }

            // Se ejecuta si se produce un error de red
            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Log.e("REPOSITORY_MOVIES", "Error on API call: ${t.message}")
            }
        })
    }

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