package es.unex.giss.asee.ghiblitrunk.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.databinding.ItemLittleMovieBinding
import es.unex.giss.asee.ghiblitrunk.view.home.MovieViewModel

class LittleMovieAdapter (
    private var moviesList: List<Movie>,
    private val onClickItem: (Movie) -> Unit,
    private val context: Context?
): RecyclerView.Adapter<LittleMovieAdapter.LittleMovieViewHolder>() {
    class LittleMovieViewHolder(
        private val binding: ItemLittleMovieBinding,
        private val onClickItem: (Movie) ->Unit,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding){// Asignamos las características del item
                showPoster(movie.title)

                // Mostramos el título
                tvTitle.text = movie.title
                tvReleaseDate.text = movie.release_date
                tvDirector.text = movie.director

                // Configurar el clic al pulsar en el CardView
                root.setOnClickListener{
                    onClickItem(movie)
                }
            }
        }

        private fun showPoster(title: String){
            // Mostramos la imagen del siguiente formato
            val imageName = "poster_" + title.lowercase()?.replace(" ", "_")?.replace("'", "")
            // Obtener el ID de la imagen
            val resourceId = context?.resources?.getIdentifier(imageName, "drawable", context.packageName)
            Log.e("LITTLE_MOVIE_ADAPTER", "El ID del recurso para $imageName es: $resourceId")

            with(binding){
                if (resourceId != null && resourceId != 0) {
                    // Si encontramos el recurso lo añadimos al imageView
                    context?.let {
                        Glide.with(it)
                            .load(resourceId)
                            .into(ivImage)
                    }
                } else {
                    // Si no se encuentra, ocultamos el ImageView
                    ivImage.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LittleMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLittleMovieBinding.inflate(inflater, parent, false)
        return LittleMovieViewHolder(binding, onClickItem, context)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: LittleMovieViewHolder, position: Int) {
        val movie = moviesList[position]
        holder.bind(movie)
    }

    fun updateData(newList: List<Movie>) {
        // Actualizamos la lista que usa el adaptador
        moviesList = newList
        notifyDataSetChanged()
    }
}