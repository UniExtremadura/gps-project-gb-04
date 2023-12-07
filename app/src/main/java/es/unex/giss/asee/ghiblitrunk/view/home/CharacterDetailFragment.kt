package es.unex.giss.asee.ghiblitrunk.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentCharacterDetailBinding

class CharacterDetailFragment : Fragment() {

    private val args: CharacterDetailFragmentArgs by navArgs()
    private var _binding: FragmentCharacterDetailBinding?=null
    val binding
        get() = _binding!!

    private val characterViewModel: CharacterViewModel by viewModels { CharacterViewModel.Factory }

    private val TAG = "CharacterDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Utilizamos View Binding para inflar el diseño
        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val character = args.character

        characterViewModel.fetchMovieDetail(character)

        Log.d(TAG, "Fetching ${character.name} details")
        characterViewModel.characterDetail.observe(viewLifecycleOwner) { character ->
            showBinding(character)
            Log.d(TAG,"Showing ${character.name} details")
        }
    }

    private fun showBinding(character: Character?){
        with(binding){
            tvName.text = character?.name
            tvGender.text = character?.gender
            tvOtherInfo.text = "This character is ${character?.age} years old and has ${character?.eye_color?.lowercase()} eyes and ${character?.hair_color?.lowercase()} hair."
            // TODO: mostrar las películas de las que forma parte
        }
    }
}