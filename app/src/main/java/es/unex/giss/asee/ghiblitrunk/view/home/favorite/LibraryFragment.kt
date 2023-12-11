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
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentLibraryBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.LibraryPagerAdapter


class LibraryFragment : Fragment() {

    // Para la visualización del TABLAYOUT para películas y personajes
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private var _binding: FragmentLibraryBinding?=null
    val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Utilizamos View Binding para inflar el diseño
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TabLayout
        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        setUpTabLayout()
        //loadFavorites()
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