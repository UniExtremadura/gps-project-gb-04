package es.unex.giss.asee.ghiblitrunk.view.home.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentFavMoviesBinding
import es.unex.giss.asee.ghiblitrunk.login.UserManager
import es.unex.giss.asee.ghiblitrunk.view.adapters.MovieAdapter
import es.unex.giss.asee.ghiblitrunk.view.home.MovieViewModel
import kotlinx.coroutines.launch

class FavMoviesFragment : Fragment() {
    private lateinit var adapter: MovieAdapter
    private lateinit var listener: OnMovieClickListener

    private var _binding: FragmentFavMoviesBinding?=null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels { MovieViewModel.Factory }


    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch { viewModel.user = UserManager.loadCurrentUser(requireContext()) }

        setUpRecyclerView(emptyList())
        subscribeUI(adapter)
    }

    private fun subscribeUI(adapter: MovieAdapter){
        viewModel.updateFavoritesMovies()
        viewModel.favoriteMovies.observe(viewLifecycleOwner) { favorites ->
            adapter.updateData(favorites)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentFavMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpiar el binding
    }

    private fun setUpRecyclerView(moviesList: List<Movie>){
        // Actualizar el RecyclerView con la lista combinada
        adapter = MovieAdapter(
            moviesList,
            viewModel = viewModel,
            onClickItem =
            {
                listener.onMovieClick(it)
            },
            context = context
        )

        with(binding){
            rvMoviesList.layoutManager = LinearLayoutManager(context)
            rvMoviesList.adapter = adapter
        }
    }

}