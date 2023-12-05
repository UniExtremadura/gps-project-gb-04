package es.unex.giss.asee.ghiblitrunk.view.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import es.unex.giss.asee.ghiblitrunk.api.RetrofitClient
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.database.GhibliTrunkDatabase
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentMovieDetailBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var db: GhibliTrunkDatabase
    private lateinit var repository: Repository

    private var _binding: FragmentMovieDetailBinding?=null
    private val binding get() = _binding!!

    private lateinit var listener: OnCommentClickListener

    interface OnCommentClickListener {
        fun onCommentClick(movies: Movie, fragmentId: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Utilizamos View Binding para inflar el diseño
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = GhibliTrunkDatabase.getInstance(context)!!
        repository = Repository.getInstance(db.characterDao(), db.movieDao(), RetrofitClient.apiService)

        if(context is OnCommentClickListener){
            listener = context
        } else {
            throw RuntimeException("$context must implement OnCommentClickListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie = args.movie

        lifecycleScope.launch {
            Log.d("MovieDetailFragment", "Fetching ${movie.title} details")
            try {
                val _movie = repository.fetchMovieDetail(movie.id)
                _movie?.isFavourite = movie.isFavourite
                showBinding(_movie)
            }catch (exception: Exception){
                Exception("CharacterDetailFragment error: ${exception.message}")
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
        Log.d("MovieDetailFragment","Showing ${movie.title} details")
    }

    private fun showBinding(movie: Movie?){
        with(binding){
            /*
            // Establecer valores al título y a la descripción
            tvTitle.text = movie.title
            tvContent.text = movie.content

            // Configurar el like si existe

            // Configurar el botón de comentar
            btnComment.setOnClickListener {
                listener.onCommentClick(news, R.id.movieDetailFragment)
            }
            */

            // TODO: Hacer el resto de bindings
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MovieDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}