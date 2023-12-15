package es.unex.giss.asee.ghiblitrunk.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giss.asee.ghiblitrunk.GhibliTrunkApplication
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Comment
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import kotlinx.coroutines.launch

class CommentViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>>
        get() = _comments

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    fun onToastShown(){
        _toast.value = null
    }

    fun addCommentToMovie(movieId: String, userId: Long, commentText: String) {
        viewModelScope.launch {
            repository.addCommentToMovie(movieId, userId, commentText)
            _toast.value = "Thanks for the comment!"
            updateCommentsForMovie(movieId)
        }
    }

    fun updateCommentsForMovie(movieId: String) {
        viewModelScope.launch {
            val comments = repository.getCommentsForMovie(movieId)
            _comments.postValue(comments)
        }
    }

    // TODO: permitir al usuario eliminar un comentario
    fun deleteCommentFromMovie(movieId: String, commentId: String) {
        viewModelScope.launch {
            // crear metodo en repository para borrar y actualizar la lista de comentarios
        }
    }

    fun isValidComment(comment: String): Boolean {
        // Verificar que el comentario tenga entre 1 y 100 caracteres
        return if(comment.length in 1..100){
            true
        }else{
            _toast.value = "The comment must be between 1 and 100 characters."
            false
        }
    }

    fun insertAndRelate(movie: Movie, userId: Long) {
        viewModelScope.launch {
            repository.insertAndRelate(movie, userId)
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

                return CommentViewModel(
                    (application as GhibliTrunkApplication).appContainer.repository,
                ) as T
            }
        }

    }
}