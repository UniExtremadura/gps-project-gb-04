package es.unex.giss.asee.ghiblitrunk.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentMoviesBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.MovieAdapter


class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: MovieAdapter

    private val viewModel: MovieViewModel by viewModels { MovieViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Utilizamos View Binding para inflar el diseño
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        // Establecer listeners
        setupListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

        viewModel.spinner.observe(viewLifecycleOwner) { movie ->
            // TODO: Poner un spinner en la interfaz gráfica
            //binding.spinner.visibility = if (movie) View.VISIBLE else View.GONE
        }

        viewModel.toast.observe(viewLifecycleOwner) {text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        setUpRecyclerView(emptyList())
        subscribeUI(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }

    //endregion

    private fun subscribeUI(adapter: MovieAdapter){
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.updateData(movies)
        }
    }

    private fun setupListeners() {
        // Gestión de los filtros
        with(binding){
            ibFilter.setOnClickListener {
                // TODO: iniciar el activity de filtros
                // val intent = Intent(activity, FilterActivity::class.java)
                //startActivity(intent)
            }
        }
    }


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