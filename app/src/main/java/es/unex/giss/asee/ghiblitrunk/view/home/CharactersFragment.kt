package es.unex.giss.asee.ghiblitrunk.view.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.GhibliTrunkApplication
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentCharactersBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.CharacterAdapter
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CharactersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharactersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentCharactersBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: CharacterAdapter
    private lateinit var listener: OnCharacterClickListener

    private lateinit var repository: Repository

    interface OnCharacterClickListener {
        fun onCharacterClick(character: Character)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        val appContainer = (this.activity?.application as GhibliTrunkApplication).appContainer
        repository = appContainer.repository

        if(context is OnCharacterClickListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + " must implement onNewsClickListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView(emptyList())
        subscribeUI(adapter)
        launchDataLoad { repository.tryUpdateRecentDataCache() }
    }

    private fun subscribeUI(adapter: CharacterAdapter){
        repository.characters.observe(viewLifecycleOwner) { characters ->
            adapter.updateData(characters)
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

    private fun setUpRecyclerView(charactersList: List<Character>){
        // Actualizar el RecyclerView con la lista combinada
        adapter = CharacterAdapter(
            charactersList,
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CharactersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CharactersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}