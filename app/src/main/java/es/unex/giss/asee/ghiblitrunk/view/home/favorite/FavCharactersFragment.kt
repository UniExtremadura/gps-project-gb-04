package es.unex.giss.asee.ghiblitrunk.view.home.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentFavCharactersBinding
import es.unex.giss.asee.ghiblitrunk.login.UserManager
import es.unex.giss.asee.ghiblitrunk.view.adapters.CharacterAdapter
import es.unex.giss.asee.ghiblitrunk.view.home.CharacterViewModel
import kotlinx.coroutines.launch


class FavCharactersFragment : Fragment() {
    private lateinit var adapter: CharacterAdapter
    private lateinit var listener: OnCharacterClickListener

    private var _binding: FragmentFavCharactersBinding?=null
    private val binding get() = _binding!!

    private val viewModel: CharacterViewModel by viewModels { CharacterViewModel.Factory }


    interface OnCharacterClickListener {
        fun onCharacterClick(character: Character)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentFavCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch { viewModel.user = UserManager.loadCurrentUser(requireContext()) }

        setUpRecyclerView(emptyList())
        subscribeUI(adapter)
    }

    private fun subscribeUI(adapter: CharacterAdapter){
        viewModel.updateFavoritesCharacters()
        viewModel.favoriteCharacters.observe(viewLifecycleOwner) { favorites ->
            adapter.updateData(favorites)
        }
    }


    private fun setUpRecyclerView(characterList: List<Character>){
        // Actualizar el RecyclerView con la lista combinada
        adapter = CharacterAdapter(
            characterList,
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpiar el binding
    }

}