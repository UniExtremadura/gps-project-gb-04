package es.unex.giss.asee.ghiblitrunk.view.home.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentFavMoviesBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.MovieAdapter
import es.unex.giss.asee.ghiblitrunk.view.home.HomeViewModel
import es.unex.giss.asee.ghiblitrunk.view.home.MovieViewModel

class FavMoviesFragment : Fragment() {
    private lateinit var adapter: MovieAdapter

    private var _binding: FragmentFavMoviesBinding?=null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels { MovieViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    //region Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

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

    //endregion

    private fun setUpRecyclerView(moviesList: List<Movie>){
        // Actualizar el RecyclerView con la lista combinada
        adapter = MovieAdapter(
            moviesList,
            viewModel = viewModel,
            onClickItem =
            {
                homeViewModel.onMovieClick(it)
            },
            context = context
        )

        with(binding){
            rvMoviesList.layoutManager = LinearLayoutManager(context)
            rvMoviesList.adapter = adapter
        }
    }

}