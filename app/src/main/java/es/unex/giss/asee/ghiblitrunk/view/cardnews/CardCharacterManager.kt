package es.unex.giss.asee.ghiblitrunk.view.cardnews

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import es.unex.giss.asee.ghiblitrunk.api.RetrofitClient
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.database.GhibliTrunkDatabase
import es.unex.giss.asee.ghiblitrunk.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardCharacterManager(private val context: Context) {

    var db: GhibliTrunkDatabase = GhibliTrunkDatabase.getInstance(context)
    var repository: Repository = Repository.getInstance(db.characterDao(), db.movieDao(), RetrofitClient.apiService)

    fun onClickLike(character: Character) {
        CoroutineScope(Dispatchers.Main).launch {
            if (repository.getIfFavorite(character))
                unsetFavorite(character)
            else
                setFavorite(character)
        }
    }

    fun onClickLike(movie: Movie) {
        CoroutineScope(Dispatchers.Main).launch {
            if (repository.getIfFavorite(movie))
                unsetFavorite(movie)
            else
                setFavorite(movie)
        }
    }

    private fun setFavorite(character: Character){
        CoroutineScope(Dispatchers.Main).launch{
            UserManager.loadCurrentUser(context)?.userId?.let {
                repository.characterToLibrary(
                    character,
                    it
                )
            }
            Toast.makeText(context, "Character liked!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun unsetFavorite(character: Character){
        CoroutineScope(Dispatchers.Main).launch{
            UserManager.loadCurrentUser(context)?.userId?.let {
                repository.deleteCharacterFromLibrary(
                    character,
                    it
                )
            }
            Toast.makeText(context, "Character unliked!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFavorite(movie: Movie){
        CoroutineScope(Dispatchers.Main).launch{
            UserManager.loadCurrentUser(context)?.userId?.let {
                repository.movieToLibrary(
                    movie,
                    it
                )
            }
            Toast.makeText(context, "Movie liked!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun unsetFavorite(movie: Movie){
        CoroutineScope(Dispatchers.Main).launch{
            UserManager.loadCurrentUser(context)?.userId?.let {
                repository.deleteMovieFromLibrary(
                    movie,
                    it
                )
            }
            Toast.makeText(context, "Movie unliked!", Toast.LENGTH_SHORT).show()
        }
    }
}
