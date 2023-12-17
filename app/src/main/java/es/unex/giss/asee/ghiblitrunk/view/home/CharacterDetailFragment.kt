package es.unex.giss.asee.ghiblitrunk.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentCharacterDetailBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.CharacterAdapter
import es.unex.giss.asee.ghiblitrunk.view.adapters.LittleMovieAdapter

class CharacterDetailFragment : Fragment() {

    private val args: CharacterDetailFragmentArgs by navArgs()
    private var _binding: FragmentCharacterDetailBinding?=null
    val binding get() = _binding!!
    private lateinit var adapter: LittleMovieAdapter

    private val characterViewModel: CharacterViewModel by viewModels { CharacterViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

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

        characterViewModel.fetchCharacterDetail(character)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            characterViewModel.user = user
        }

        characterViewModel.toast.observe(viewLifecycleOwner) {text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                characterViewModel.onToastShown()
            }
        }

        Log.d(TAG, "Fetching ${character.name} details")
        characterViewModel.characterDetail.observe(viewLifecycleOwner) { character ->
            showBinding(character)
            Log.d(TAG,"Showing ${character.name} details")
        }
    }

    private fun showBinding(character: Character?) {
        with(binding) {
            tvName.text = character?.name
            tvGender.text = character?.gender
            tvOtherInfo.text = "This character is ${character?.age} years old and has ${character?.eye_color?.lowercase()} eyes and ${character?.hair_color?.lowercase()} hair."

            // Obtenemos el ID de la imagen a mostrar
            val imageName = "portrait_" + character?.name?.lowercase()?.replace(" ", "_")?.replace("'", "")
            val imageId = context?.resources?.getIdentifier(imageName, "drawable", context?.packageName)

            // Se muestra si se encuentra la imagen asociada al personaje
            if (imageId != null && imageId != 0) {
                ivPortrait.setImageResource(imageId)
            } else {
                // Si no se encuentra, ocultamos el ImageView
                binding.ivPortrait.visibility = View.GONE
            }

            // Mostrar las películas relacionadas
            if (character != null) {
                characterViewModel.getMoviesRelated(character.films).observe(viewLifecycleOwner) { moviesRelated ->
                    moviesRelated?.let {
                        setUpRecyclerView(it)
                    }
                }
            }

            // Configurar el botón de like
            ivLike.setOnClickListener {
                if (character != null) {
                    characterViewModel.onClickLike(character)
                }
            }
        }
    }

    private fun setUpRecyclerView(movieList: List<Movie>){
        // Actualizar el RecyclerView con la lista combinada
        adapter = LittleMovieAdapter(
            movieList,
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