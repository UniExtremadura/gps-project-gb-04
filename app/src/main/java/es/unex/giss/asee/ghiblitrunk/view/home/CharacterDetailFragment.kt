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
import es.unex.giss.asee.ghiblitrunk.GhibliTrunkApplication
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentCharacterDetailBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val args: CharacterDetailFragmentArgs by navArgs()
    private lateinit var repository: Repository

    private var _binding: FragmentCharacterDetailBinding?=null
    private val binding get() = _binding!!

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
        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val appContainer = (this.activity?.application as GhibliTrunkApplication).appContainer
        repository = appContainer.repository
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val character = args.character

        lifecycleScope.launch {
            Log.d("CharacterDetailFragment", "Fetching ${character.name} details")
            try {
                val _character = repository.fetchCharacterDetail(character.id)
                _character?.isFavourite = character.isFavourite
                showBinding(_character)
            }catch (exception: Exception){
                Exception("CharacterDetailFragment error: ${exception.message}")
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
        Log.d("CharacterDetailFragment","Showing ${character.name} details")
    }

    private fun showBinding(character: Character?){
        with(binding){
            tvName.text = character?.name
            tvGender.text = character?.gender
            tvOtherInfo.text = "This character is ${character?.age} years old and has ${character?.eye_color?.lowercase()} eyes and ${character?.hair_color?.lowercase()} hair."
            // TODO: mostrar las películas de las que forma parte
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CharacterDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CharacterDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}