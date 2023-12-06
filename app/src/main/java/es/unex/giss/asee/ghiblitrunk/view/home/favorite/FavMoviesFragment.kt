package es.unex.giss.asee.ghiblitrunk.view.home.favorite

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.R
import es.unex.giss.asee.ghiblitrunk.api.RetrofitClient
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.database.GhibliTrunkDatabase
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentFavMoviesBinding
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentLibraryBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.MovieAdapter
import es.unex.giss.asee.ghiblitrunk.view.home.MoviesFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavMoviesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var db: GhibliTrunkDatabase
    private lateinit var repository: Repository

    private lateinit var adapter: MovieAdapter
    private lateinit var listener: OnMovieClickListener

    private var _binding: FragmentFavMoviesBinding?=null
    private val binding get() = _binding!!

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = GhibliTrunkDatabase.getInstance(context)
        repository = Repository.getInstance(db.characterDao(), db.movieDao(), RetrofitClient.apiService)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView(emptyList())
        subscribeUI(adapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_movies, container, false)
    }

    private fun subscribeUI(adapter: MovieAdapter){
        repository.moviesInLibrary.observe(viewLifecycleOwner) { moviesInLibrary ->
            adapter.updateData(moviesInLibrary.movies)
        }
    }

    private fun setUpRecyclerView(moviesList: List<Movie>){
        // Actualizar el RecyclerView con la lista combinada
        adapter = MovieAdapter(
            moviesList,
            onClickItem =
            {
                listener.onMovieClick(it)
            },
            context = context
        )

        with(binding){
            /*
            rvMoviesList.layoutManager = LinearLayoutManager(context)
            rvMoviesList.adapter = adapter
             */
            // TODO: Hacer el resto de bindings.
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavMoviesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}