package es.unex.giss.asee.ghiblitrunk.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import es.unex.giss.asee.ghiblitrunk.R
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.databinding.ItemMovieBinding
import es.unex.giss.asee.ghiblitrunk.view.cardnews.CardCharacterManager

class MovieAdapter (
    private var moviesList: List<Movie>,
    private val onClickItem: (Movie) -> Unit,
    private val context: Context?
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onClickItem: (Movie) ->Unit,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {
        private val cardManager = context?.let { CardCharacterManager(it) }
        fun bind(movie: Movie) {
            with(binding){// Asignamos las características del item
                // Mostramos la imagen
                val imageName = movie.title.lowercase().replace(" ", "_").replace("'", "") // Formato para buscar la imagen
                // Obtener el ID de la imagen
                val resourceId = context?.resources?.getIdentifier(imageName, "drawable", context.packageName)
                Log.e("MOVIE_ADAPTER", "El ID del recurso para $imageName es: $resourceId")

                if (resourceId != null && resourceId != 0) {
                    // Si encontramos el recurso lo añadimos al imageView
                    context?.let {
                        Glide.with(it)
                            .load(resourceId)
                            .into(ivImage)
                    }
                } else {
                    // Si no se encuentra, ocultamos el ImageView
                    binding.ivImage.visibility = View.GONE
                }

                // Mostramos el título
                tvTitle.text = movie.title
                tvDescription.text = movie.description

                // Configuramos el like
                ivLike.setOnClickListener {
                    cardManager?.onClickLike(movie)
                }


                // Configurar el clic al pulsar en el resto de items del card_view
                root.setOnClickListener{
                    onClickItem(movie)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding, onClickItem, context)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = moviesList[position]

        holder.bind(movie)
    }

    fun updateData(newList: List<Movie>) {
        // Actualizamos la lista que usa el adaptador
        moviesList = newList
        notifyDataSetChanged()
    }
}