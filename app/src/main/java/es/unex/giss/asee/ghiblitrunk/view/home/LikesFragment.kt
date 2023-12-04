package es.unex.giss.asee.ghiblitrunk.view.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import es.unex.giss.asee.ghiblitrunk.R
import es.unex.giss.asee.ghiblitrunk.database.GhibliTrunkDatabase
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentLikesBinding
import kotlinx.coroutines.launch
import java.lang.RuntimeException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LikesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LikesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var db: GhibliTrunkDatabase
    private var _binding: FragmentLikesBinding?=null
    private val binding get() = _binding!!
    /*
    private lateinit var adapter: NewsAdapter

    private lateinit var listener: OnNewsClickListener
    interface OnNewsClickListener {
        fun onNewsClick(news: News, fragmentId: Int)
    }

    // Lista de las noticias a las que el usuario le ha dado like
    private var favNews = emptyList<News>()

     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /*
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Utilizamos View Binding para inflar el diseño
        _binding = FragmentLikesBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = WhichNewsDatabase.getInstance(context)!!
        if(context is OnNewsClickListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + " must implement onNewsClickListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        setUpRecyclerView()
        loadFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
    private fun setUpRecyclerView() {
        adapter = NewsAdapter(
            favNews,
            onClickItem =
            {
                listener.onNewsClick(it, R.id.likesFragment)
            },
            context = context
        )

        with(binding) {
            rvLikesList.layoutManager = LinearLayoutManager(context)
            rvLikesList.adapter = adapter
        }
    }

    // TODO: convertir en extensión como para obtener el historial de noticias
    private fun loadFavorites(){
        lifecycleScope.launch {
            UserManager.loadCurrentUser(requireContext())?.userId?.let { userId ->
                favNews = db.newsDao().getFavouriteNewsOfUser(userId)
            }
            Log.d("LIKES_FRAGMENT", "Favorites List Size: $favNews.size")
            adapter.updateData(favNews)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoritesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LikesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

     */
}