package es.unex.giss.asee.ghiblitrunk.view.home.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import es.unex.giss.asee.ghiblitrunk.R
import es.unex.giss.asee.ghiblitrunk.database.GhibliTrunkDatabase
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentLibraryBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.LibraryPagerAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LikesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LibraryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    // Para la visualización del TABLAYOUT para películas y personajes
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private lateinit var db: GhibliTrunkDatabase
    private var _binding: FragmentLibraryBinding?=null
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Utilizamos View Binding para inflar el diseño
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = GhibliTrunkDatabase.getInstance(context)!!
        /*
        if(context is OnNewsClickListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + " must implement onNewsClickListener")
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TabLayout
        viewPager = binding.viewPager
        tabLayout = binding.tabLayout
        // Después de inicializar tabLayout
        tabLayout.setSelectedTabIndicator(R.drawable.tab_indicator)
        setUpTabLayout()
        //loadFavorites()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }

    private fun setUpTabLayout() {
        // Crear un adapter para el ViewPager
        val adapter = LibraryPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        // Asociar el TabLayout con el ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Movie"
                else -> "Characters"
            }
        }.attach()
    }
}