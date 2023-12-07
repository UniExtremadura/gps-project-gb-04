package es.unex.giss.asee.ghiblitrunk.view.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentCharactersBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.CharacterAdapter
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import es.unex.giss.asee.ghiblitrunk.login.UserManager
import kotlinx.coroutines.launch

class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: CharacterAdapter
    private lateinit var listener: OnCharacterClickListener
    private val viewModel: CharacterViewModel by viewModels { CharacterViewModel.Factory }

    interface OnCharacterClickListener {
        fun onCharacterClick(character: Character)
    }


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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is OnCharacterClickListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + " must implement OnCharacterClickListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch { viewModel.user = UserManager.loadCurrentUser(requireContext()) }

        viewModel.spinner.observe(viewLifecycleOwner) { character ->
            // TODO: Poner un spinner en la interfaz gráfica
            //binding.spinner.visibility = if (character) View.VISIBLE else View.GONE
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

    private fun subscribeUI(adapter: CharacterAdapter){
        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            adapter.updateData(characters)
        }
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

    private fun setUpRecyclerView(charactersList: List<Character>){
        // Actualizar el RecyclerView con la lista combinada
        adapter = CharacterAdapter(
            charactersList,
            viewModel = viewModel,
            onClickItem =
            {
                listener.onCharacterClick(it)
            },
            context = context
        )

        with(binding){
            rvCharactersList.layoutManager = LinearLayoutManager(context)
            rvCharactersList.adapter = adapter
        }
    }

}