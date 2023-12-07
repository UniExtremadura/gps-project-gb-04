package es.unex.giss.asee.ghiblitrunk.view.home

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

    init {
        refresh()
    }

    private fun refresh() {
        launchDataLoad { repository.tryUpdateRecentDataCache() }
    }

    fun onToastShown(){
        _toast.value = null
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
        }
    }

    fun updateFavoritesMovies() {
        viewModelScope.launch {
            val favorites = repository.getFavoritesMovies()
            _favoriteMovies.value = favorites
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