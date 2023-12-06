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

    // Obtener el ID de la imagen por su nombre
    private fun getImageResourceId(imageName: String): Int {
        return context?.resources?.getIdentifier(
            imageName,
            "drawable",
            "${context.packageName}.movie_wallpapers"
        ) ?: 0
    }

    // Lista de imágenes que cargará el adapter
    private val wallpapersList = mutableListOf<Int>()

    init {
        // Cargar las imágenes en la lista usando el patrón de nombres
        val resources = context?.resources
        if (resources != null) {
            for (i in 1..10) {
                val imageName = "img_movie_$i"
                val imageId = getImageResourceId(imageName)
                if (imageId != 0) {
                    wallpapersList.add(imageId)
                }
            }
        }
    }

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onClickItem: (Movie) ->Unit,
        private val context: Context?,
        private val wallpapersList: List<Int>
    ) : RecyclerView.ViewHolder(binding.root) {
        private val cardManager = context?.let { CardCharacterManager(it) }
        fun bind(movie: Movie, position: Int) {
            with(binding){// Asignamos las características del item
                if(position != -1){
                    // Asignar la imagen al ImageView
                    ivImage.setImageResource(wallpapersList[position])
                }


                tvTitle.text = movie.title
                tvDescription.text = movie.description

                // Configurar onClick
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
        return MovieViewHolder(binding, onClickItem, context, wallpapersList)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = moviesList[position]

        // Verificar si wallpapersList tiene elementos antes de realizar cálculos
        if (wallpapersList.isNotEmpty()) {
            val imagePosition = position % wallpapersList.size
            holder.bind(movie, imagePosition)
        } else {
            // Manejar el caso donde wallpapersList está vacía
            // Por ejemplo, puedes asignar una imagen predeterminada
            holder.bind(movie, -1) // O asignar el índice que desees para la imagen
        }
    }

    fun updateData(newList: List<Movie>) {
        // Actualizamos la lista que usa el adaptador
        moviesList = newList
        notifyDataSetChanged()
    }
}