package es.unex.giss.asee.ghiblitrunk.view.home.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentFavCharactersBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.CharacterAdapter
import es.unex.giss.asee.ghiblitrunk.view.home.CharacterViewModel
import es.unex.giss.asee.ghiblitrunk.view.home.HomeViewModel


class FavCharactersFragment : Fragment() {
    private lateinit var adapter: CharacterAdapter

    private var _binding: FragmentFavCharactersBinding?=null
    private val binding get() = _binding!!

    private val viewModel: CharacterViewModel by viewModels { CharacterViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    //region Lifecycle

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

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

        setUpRecyclerView(emptyList())
        subscribeUI(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpiar el binding
    }

    //endregion

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