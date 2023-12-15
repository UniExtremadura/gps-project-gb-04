package es.unex.giss.asee.ghiblitrunk.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giss.asee.ghiblitrunk.GhibliTrunkApplication
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.data.models.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieViewModel (
    private val repository: Repository
): ViewModel() {
    var user: User? = null
    val movies = repository.movies

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    private val _favoriteMovies = MutableLiveData<List<Movie>>()
    val favoriteMovies: LiveData<List<Movie>>
        get() = _favoriteMovies

    private val _movieDetail = MutableLiveData<Movie>()
    val movieDetail: LiveData<Movie>
        get() = _movieDetail

    init {
        refresh()
    }

    private fun refresh() {
        launchDataLoad { repository.tryUpdateRecentDataCache() }
    }

    fun onToastShown(){
        viewModelScope.launch {
            _toast.value = null
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            }catch (exception: Exception){
                Exception("MoviesFragment error: ${exception.message}")
                _toast.value = exception.message
            }finally {
                _spinner.value = false
            }
        }
    }

    fun onClickLike(movie: Movie) {
        viewModelScope.launch {
            if (repository.getIfFavorite(movie))
                unsetFavorite(movie)
            else
                setFavorite(movie)
        }
    }

    private fun setFavorite(movie: Movie){
        viewModelScope.launch {
            repository.movieToLibrary(movie, user!!.userId!!)
            _toast.value = "Movie liked!"
        }
    }

    private fun unsetFavorite(movie: Movie){
        viewModelScope.launch {
            repository.deleteMovieFromLibrary(movie, user!!.userId!!)
            _toast.value = "Movie unliked!"
            updateFavoritesMovies()
        }
    }

    fun updateFavoritesMovies() {
        viewModelScope.launch {
            val favorites = repository.getFavoritesMovies()
            Log.e("MOVIE_VIEW_MODEL", favorites.toString())
            _favoriteMovies.value = favorites
        }
    }

    fun fetchMovieDetail(movie: Movie){
        viewModelScope.launch {
            _movieDetail.value = repository.fetchMovieDetail(movie.id)
        }
    }

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>>
        get() = _searchResults

    var currentFilter: String = "" // Esta variable guarda el filtro seleccionado

    fun setSearchFilter(filter: String) {
        currentFilter = filter
    }

    fun searchMoviesByFilter(query: String) {
        viewModelScope.launch {
            val searchResults = when (currentFilter) {
                "Search by Title" -> repository.searchMoviesByTitle(query)
                "Search by Date" -> repository.searchMoviesByDate(query)
                "Search by Director" -> repository.searchMoviesByDirector(query)
                else -> MutableLiveData<List<Movie>>()
            }

            searchResults.observeForever { movies ->
                _searchResults.value = movies
            }
        }
    }

    fun setMovieDetail(movie: Movie){
        viewModelScope.launch {
            _movieDetail.value = movie
        }
    }

    companion object{

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return MovieViewModel(
                    (application as GhibliTrunkApplication).appContainer.repository,
                ) as T
            }
        }

    }
}