package es.unex.giss.asee.ghiblitrunk.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.data.models.User
import es.unex.giss.asee.ghiblitrunk.data.models.Character


class HomeViewModel: ViewModel() {
    private val _user = MutableLiveData<User>(null)
    val user: LiveData<User>
        get() = _user

    var userInSession: User? = null
        set(value) {
            field = value
            _user.value = value!!
        }

    private val _navigateToMovie = MutableLiveData<Movie>(null)
    val navigateToMovie: LiveData<Movie>
        get() = _navigateToMovie

    fun onMovieClick(movie: Movie){
        _navigateToMovie.value = movie
    }

    private val _navigateToCharacter = MutableLiveData<Character>(null)
    val navigateToCharacter: LiveData<Character>
        get() = _navigateToCharacter

    fun onCharacterClick(character: Character){
        _navigateToCharacter.value = character
    }

    private val _navigateToComments = MutableLiveData<Movie>(null)
    val navigateToComments: LiveData<Movie>
        get() = _navigateToComments

    fun onCommentClick(movie: Movie){
        _navigateToComments.value = movie
    }


}