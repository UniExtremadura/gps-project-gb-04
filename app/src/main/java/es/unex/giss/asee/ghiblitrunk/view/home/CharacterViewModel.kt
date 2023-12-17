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
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.data.models.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CharacterViewModel (
    private val repository: Repository
): ViewModel() {
    var user: User? = null
    val characters = repository.characters

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    private val _favoriteCharacters = MutableLiveData<List<Character>>()
    val favoriteCharacters: LiveData<List<Character>>
        get() = _favoriteCharacters

    private val _characterDetail = MutableLiveData<Character>()
    val characterDetail: LiveData<Character>
        get() = _characterDetail

    private var lastQuery: String = ""

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

    fun onClickLike(character: Character) {
        viewModelScope.launch {
            if (repository.getIfFavorite(character))
                unsetFavorite(character)
            else
                setFavorite(character)

            delay(100)
            searchCharactersByFilter()
        }

    }

    private fun setFavorite(character: Character){
        viewModelScope.launch {
            repository.characterToLibrary(character, user!!.userId!!)
            _toast.value = "Character liked!"
        }
    }

    private fun unsetFavorite(character: Character){
        viewModelScope.launch {
            repository.deleteCharacterFromLibrary(character, user!!.userId!!)
            _toast.value = "Character unliked!"
            updateFavoritesCharacters()
        }
    }

    fun updateFavoritesCharacters() {
        viewModelScope.launch {
            val favorites = repository.getFavoritesCharacters()
            Log.e("CHARACTER_VIEW_MODEL", favorites.toString())
            _favoriteCharacters.postValue(favorites)
        }
    }

    fun fetchCharacterDetail(character: Character){
        viewModelScope.launch {
            _characterDetail.value = repository.fetchCharacterDetail(character.id)
        }
    }

    fun setCharacterDetail(character: Character){
        viewModelScope.launch {
            _characterDetail.value = character
        }
    }

    private val _searchResults = MutableLiveData<List<Character>>()
    val searchResults: LiveData<List<Character>>
        get() = _searchResults

    var currentFilter: String = "Search by Name"

    fun setSearchFilter(filter: String) {
        currentFilter = filter
    }

    fun searchCharactersByFilter(query: String) {
        lastQuery = query

        viewModelScope.launch {
            val searchResults = when (currentFilter) {
                "Search by Name" -> repository.searchCharactersByName(query)
                "Search by Age" -> repository.searchCharactersByAge(query)
                "Search by Gender" -> repository.searchCharactersByGender(query)
                else -> MutableLiveData<List<Character>>()
            }

            searchResults.observeForever { characters ->
                _searchResults.value = characters
            }
        }
    }

    fun searchCharactersByFilter() {

        viewModelScope.launch {
            val searchResults = when (currentFilter) {
                "Search by Name" -> repository.searchCharactersByName(lastQuery)
                "Search by Age" -> repository.searchCharactersByAge(lastQuery)
                "Search by Gender" -> repository.searchCharactersByGender(lastQuery)
                else -> MutableLiveData<List<Character>>()
            }

            searchResults.observeForever { characters ->
                _searchResults.value = characters
            }
        }
    }

    fun getMoviesRelated(moviesUrls: List<String>): LiveData<List<Movie>> {
        val moviesRelated = MutableLiveData<List<Movie>>()

        viewModelScope.launch {
            val moviesList = repository.getMoviesRelated(moviesUrls)
            Log.d("VIEWMODEL", "el tamaño de la lista es ${moviesList.size}")
            moviesList.forEachIndexed { index, movie ->
                Log.d("VIEWMODEL", "Índice $index: ${movie.title}")
            }

            moviesRelated.postValue(moviesList)
        }

        return moviesRelated
    }


    companion object{

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return CharacterViewModel(
                    (application as GhibliTrunkApplication).appContainer.repository,
                ) as T
            }
        }

    }
}