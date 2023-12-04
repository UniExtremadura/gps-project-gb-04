package es.unex.giss.asee.ghiblitrunk.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        fun bind(movie: Movie, totalItems: Int) {
            with(binding){
                // Asignar valores a las vistas
                tvTitle.text = movie.title
                tvDescription.text = movie.description

                // Configurar el clic en el botón de "like"
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
        return MovieAdapter.MovieViewHolder(binding, onClickItem, context)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        val movie = moviesList[position]
        holder.bind(movie, moviesList.size)
    }

    fun updateData(favMovies: List<Movie>) {
        // Actualizamos la lista que usa el adaptador
        moviesList = favMovies
        notifyDataSetChanged()
    }
}