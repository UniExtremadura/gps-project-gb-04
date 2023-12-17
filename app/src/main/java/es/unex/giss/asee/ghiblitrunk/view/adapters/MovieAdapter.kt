package es.unex.giss.asee.ghiblitrunk.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.databinding.ItemMovieBinding
import es.unex.giss.asee.ghiblitrunk.view.home.MovieViewModel

class MovieAdapter (
    private var moviesList: List<Movie>,
    private val onClickItem: (Movie) -> Unit,
    private val viewModel: MovieViewModel,
    private val context: Context?
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onClickItem: (Movie) ->Unit,
        private val viewModel: MovieViewModel,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding){// Asignamos las características del item
                // Obtenemos el ID de la imagen a mostrar
                val imageName = "poster_" + movie?.title?.lowercase()?.replace(" ", "_")?.replace("'", "")
                val imageId = context?.resources?.getIdentifier(imageName, "drawable", context.packageName)

                // Se muestra si se encuentra la imagen asociada al personaje
                if (imageId != null && imageId != 0) {
                    context?.let {
                        Glide.with(it)
                            .load(imageId)
                            .into(ivImage)
                    }
                } else {
                    // Si no se encuentra, ocultamos el ImageView
                    binding.ivImage.visibility = View.GONE
                }

                tvTitle.text = movie.title
                tvReleaseDate.text = movie.release_date
                tvDirector.text = movie.director

                // Configuramos el like
                ivLike.setOnClickListener {
                    viewModel.onClickLike(movie)
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
        return MovieViewHolder(binding, onClickItem, viewModel, context)
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