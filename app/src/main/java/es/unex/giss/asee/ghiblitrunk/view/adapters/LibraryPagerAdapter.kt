package es.unex.giss.asee.ghiblitrunk.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import es.unex.giss.asee.ghiblitrunk.view.home.favorite.FavCharactersFragment
import es.unex.giss.asee.ghiblitrunk.view.home.favorite.FavMoviesFragment

class LibraryPagerAdapter (fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    // Contamos el número de fragmentos
    override fun getItemCount(): Int = 2

    // llamamos al fragmento adecuado según la pestaña TAB
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavMoviesFragment()
            else -> FavCharactersFragment()
        }
    }
}