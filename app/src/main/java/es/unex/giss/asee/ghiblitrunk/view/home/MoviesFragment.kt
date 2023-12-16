package es.unex.giss.asee.ghiblitrunk.view.home

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.R
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentMoviesBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.MovieAdapter


class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: MovieAdapter

    private val viewModel: MovieViewModel by viewModels { MovieViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var rotateAnimation : Animation

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

        viewModel.setSearchFilter("Search by Title")


        setUpUI()
        setUpRecyclerView(emptyList())
        subscribeUI(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }

    private fun setUpUI(){
        rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate)

        binding.spinner.startAnimation(rotateAnimation)
    }

    //endregion

    private fun subscribeUI(adapter: MovieAdapter){

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

        viewModel.spinner.observe(viewLifecycleOwner) { movie ->
            //binding.spinner.visibility = if (movie) View.VISIBLE else View.GONE
        }

        viewModel.toast.observe(viewLifecycleOwner) {text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.updateData(movies)
        }
    }

    private fun setupListeners() {
        // Gestión de los filtros
        with(binding){
            ivFilter.setOnClickListener {
                showFilterDialog()
            }

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val searchText = s.toString()
                    viewModel.searchMoviesByFilter(searchText)
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            viewModel.searchResults.observe(viewLifecycleOwner) { movies ->
                adapter.updateData(movies)
            }
        }
    }

    private fun showFilterDialog() {
        var hint = ""
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_filter, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Select Filter")

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        dialogView.findViewById<RadioButton>(R.id.radioTitle).setOnClickListener {
            hint = "Search by Title"
        }

        dialogView.findViewById<RadioButton>(R.id.radioDate).setOnClickListener {
            hint = "Search by Date"
        }

        dialogView.findViewById<RadioButton>(R.id.radioDirector).setOnClickListener {
            hint = "Search by Director"
        }

        // Botón de Aceptar
        dialogView.findViewById<Button>(R.id.btnAccept).setOnClickListener {
            viewModel.setSearchFilter(hint)
            binding.etSearch.hint = hint
            alertDialog.dismiss() // Cierra el diálogo al hacer clic en "Aceptar"
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