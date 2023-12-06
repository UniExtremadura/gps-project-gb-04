package es.unex.giss.asee.ghiblitrunk.view.cardnews

import android.content.Context
import android.widget.Toast
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.database.GhibliTrunkDatabase
import es.unex.giss.asee.ghiblitrunk.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardCharacterManager(private val context: Context) {

    var db: GhibliTrunkDatabase = GhibliTrunkDatabase.getInstance(context)
    fun onClickLike(character: Character) {
        // TODO: Sustituir por código bueno
        /*
        CoroutineScope(Dispatchers.Main).launch {
            val foundNews = db.newsDao().findNews(character.url ?: "")
            if(foundNews != null){ // si la encuentro
                if (foundNews.isFavourite) unsetFavorite(foundNews) else setFavorite(foundNews)
            }else{
                if (character.isFavourite) unsetFavorite(character) else setFavorite(character)
            }
        }
        */
    }

    fun onClickLike(movie: Movie) {
        // TODO: Sustituir por código bueno
        /*
        CoroutineScope(Dispatchers.Main).launch {
            val foundNews = db.newsDao().findNews(character.url ?: "")
            if(foundNews != null){ // si la encuentro
                if (foundNews.isFavourite) unsetFavorite(foundNews) else setFavorite(foundNews)
            }else{
                if (character.isFavourite) unsetFavorite(character) else setFavorite(character)
            }
        }
        */
    }

    private fun setFavorite(character: Character){
        CoroutineScope(Dispatchers.Main).launch{
            // TODO: Cambiar por código bueno
            /*
            if(db.newsDao().findNews(news.url ?: "") != null){ // si existe la noticia
                db.newsDao().setLike(news.url, true)
            }else{ // si no existe,
                news.isFavourite = true
                UserManager.loadCurrentUser(context)?.userId?.let { userId ->
                    db.newsDao().insertAndRelate(news, userId)
                }
            }

            Toast.makeText(context, "Added to 'My Likes'", Toast.LENGTH_SHORT).show()

             */
        }
    }

    private fun unsetFavorite(character: Character){
        CoroutineScope(Dispatchers.Main).launch{
            // TODO: Cambiar por código bueno
            /*
            db.newsDao().setLike(news.url, false)
            Toast.makeText(context, "Removed from 'My Likes'", Toast.LENGTH_SHORT).show()

             */
        }
    }

    /*
    // TODO: convertir en extensión como para obtener el historial de noticias
    private fun loadFavorites(){
        lifecycleScope.launch {
            UserManager.loadCurrentUser(requireContext())?.userId?.let { userId ->
                favNews = db.newsDao().getFavouriteNewsOfUser(userId)
            }
            Log.d("LIKES_FRAGMENT", "Favorites List Size: $favNews.size")
            adapter.updateData(favNews)
        }
    }*/
}
