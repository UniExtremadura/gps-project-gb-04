package es.unex.giss.asee.ghiblitrunk.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentMovieDetailBinding

class MovieDetailFragment : Fragment() {

    private val args: MovieDetailFragmentArgs by navArgs()
    private var _binding: FragmentMovieDetailBinding?=null
    val binding
        get() = _binding!!

    private val movieViewModel: MovieViewModel by viewModels { MovieViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    private val TAG = "MovieDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Utilizamos View Binding para inflar el diseño
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie = args.movie
        movieViewModel.fetchMovieDetail(movie)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            movieViewModel.user = user
        }

        movieViewModel.toast.observe(viewLifecycleOwner) {text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                movieViewModel.onToastShown()
            }
        }

        Log.d(TAG, "Fetching ${movie.title} details")
        movieViewModel.movieDetail.observe(viewLifecycleOwner) { movie ->
            showBinding(movie)
            Log.d(TAG,"Showing ${movie.title} details")
        }
    }

    private fun showBinding(movie: Movie?){
        with(binding){
            // Establecer los detalles de la película
            tvTitle.text = movie?.title
            tvOriginalTitle.text = movie?.original_title

            // Mostramos la imagen del siguiente formato
            val imageName = "poster_" + movie?.title?.lowercase()?.replace(" ", "_")?.replace("'", "")
            // Obtener el ID de la imagen
            val resourceId = context?.resources?.getIdentifier(imageName, "drawable", context?.packageName)
            Log.e("MOVIE_DETAIL_FRAG", "El ID del recurso para $imageName es: $resourceId")

            if (resourceId != null && resourceId != 0) {
                // Si encontramos el recurso lo añadimos al imageView
                context?.let {
                    Glide.with(it)
                        .load(resourceId)
                        .into(ivPoster)
                }
            } else {
                // Si no se encuentra, ocultamos el ImageView
                binding.ivPoster.visibility = View.GONE
            }

            tvDescription.text = movie?.description

            if(movie?.director != ""){
                tvDirector.text = "Director: ${movie?.director}"
            }else{
                tvProducer.text = "Director: Anonymous"
            }

            if(movie?.producer != ""){
                tvProducer.text = "Producer: ${movie?.producer}"
            }else{
                tvProducer.text = "Producer: Anonymous"
            }

            if(movie?.release_date != ""){
                tvReleaseDate.text = "Release date: ${movie?.release_date}"
            }else{
                tvReleaseDate.text = "Release date: Anonymous"
            }

            // Configurar el botón de like
            ivLike.setOnClickListener {
                if (movie != null) {
                    movieViewModel.onClickLike(movie)
                }
            }

            // Configurar el botón de comentar
            btnWriteComment.setOnClickListener {
                movie?.let { it1 -> homeViewModel.onCommentClick(it1) }
            }
        }
    }
}