package es.unex.giss.asee.ghiblitrunk.view.home

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentCharactersBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.CharacterAdapter
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import androidx.fragment.app.viewModels
import es.unex.giss.asee.ghiblitrunk.R

class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: CharacterAdapter

    private val viewModel: CharacterViewModel by viewModels { CharacterViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Utilizamos View Binding para inflar el diseño
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)

        // Establecer listeners
        setupListeners()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Establecemos los valores de la barra de búsqueda
        binding.etSearch.hint = viewModel.currentFilter
        viewModel.setSearchFilter(viewModel.currentFilter)

        setUpRecyclerView(emptyList())
        subscribeUI(adapter)
    }

    override fun onResume() {
        super.onResume()
        binding.etSearch.text.clear() // Esto limpia el texto de la barra de búsqueda al regresar al fragmento
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }

    //endregion

    private fun subscribeUI(adapter: CharacterAdapter){
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

        viewModel.toast.observe(viewLifecycleOwner) {text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            adapter.updateData(characters)
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
                    viewModel.searchCharactersByFilter(searchText)
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
            .setTitle("Filter by...")

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        // Mostrar las opciones del filtro
        dialogView.findViewById<RadioButton>(R.id.rb_option_1).text = "Name"
        dialogView.findViewById<RadioButton>(R.id.rb_option_2).text = "Age"
        dialogView.findViewById<RadioButton>(R.id.rb_option_3).text = "Gender"

        // Mostrar la selección anterior del usuario si hubo
        when (viewModel.currentFilter) {
            "Search by Name" -> dialogView.findViewById<RadioButton>(R.id.rb_option_1).isChecked = true
            "Search by Age" -> dialogView.findViewById<RadioButton>(R.id.rb_option_2).isChecked = true
            "Search by Gender" -> dialogView.findViewById<RadioButton>(R.id.rb_option_3).isChecked = true
            // Asegúrate de manejar todos los casos posibles aquí
            else -> {} // Manejar el caso predeterminado si es necesario
        }

        // Establecer la visualización del nuevo filtrado
        dialogView.findViewById<RadioButton>(R.id.rb_option_1).setOnClickListener {
            hint = "Search by Name"
        }

        dialogView.findViewById<RadioButton>(R.id.rb_option_2).setOnClickListener {
            hint = "Search by Age"
        }

        dialogView.findViewById<RadioButton>(R.id.rb_option_3).setOnClickListener {
            hint = "Search by Gender"
        }

        // Botón de Aceptar
        dialogView.findViewById<Button>(R.id.btnAccept).setOnClickListener {
            // Limpiamos la barra de búsqueda
            binding.etSearch.text.clear()
            // Mostramos el nuevo tipo de búsqueda
            viewModel.setSearchFilter(hint)
            binding.etSearch.hint = hint
            // Cerramos el popup
            alertDialog.dismiss() // Cierra el diálogo al hacer clic en "Aceptar"
        }
    }

    private fun setUpRecyclerView(charactersList: List<Character>){
        // Actualizar el RecyclerView con la lista combinada
        adapter = CharacterAdapter(
            charactersList,
            viewModel = viewModel,
            onClickItem =
            {
                homeViewModel.onCharacterClick(it)
            },
            context = context
        )

        with(binding){
            rvCharactersList.layoutManager = LinearLayoutManager(context)
            rvCharactersList.adapter = adapter
        }
    }

}