package es.unex.giss.asee.ghiblitrunk.view.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentMoviesBinding
import es.unex.giss.asee.ghiblitrunk.login.UserManager
import es.unex.giss.asee.ghiblitrunk.view.adapters.MovieAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [MoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: MovieAdapter

    private lateinit var listener: OnMovieClickListener

    private val viewModel: MovieViewModel by viewModels { MovieViewModel.Factory }

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Utilizamos View Binding para inflar el diseño
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        // Establecer listeners
        setupListeners()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is OnMovieClickListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + " must implement OnMovieClickListener")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch { viewModel.user = UserManager.loadCurrentUser(requireContext()) }

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

    private fun subscribeUI(adapter: MovieAdapter){
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.updateData(movies)
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return lifecycleScope.launch {
            try {
                // TODO: Poner un spinner en la interfaz
                // binding.spinner.visibility = View.VISIBLE
                block()
            }catch (exception: Exception){
                Exception("MoviesFragment error: ${exception.message}")
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
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