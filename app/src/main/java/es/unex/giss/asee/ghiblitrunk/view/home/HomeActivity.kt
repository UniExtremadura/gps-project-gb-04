package es.unex.giss.asee.ghiblitrunk.view.home
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import es.unex.giss.asee.ghiblitrunk.R
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.User
import es.unex.giss.asee.ghiblitrunk.databinding.ActivityHomeBinding
import es.unex.giss.asee.ghiblitrunk.login.UserManager
import es.unex.giss.asee.ghiblitrunk.view.LoginActivity
import es.unex.giss.asee.ghiblitrunk.view.home.favorite.LibraryFragment
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(),
    MoviesFragment.OnMovieClickListener,
    CharactersFragment.OnCharacterClickListener,
    NavigationView.OnNavigationItemSelectedListener,
    MovieDetailFragment.OnCommentClickListener
{

    private lateinit var binding: ActivityHomeBinding

    private val navController by lazy{
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    private lateinit var libraryFragment: LibraryFragment
    private lateinit var moviesFragment: MoviesFragment
    private lateinit var charactersFragment: CharactersFragment

    companion object {
        const val USER_INFO = "USER_INFO"
        fun start(
            context: Context,
            user: User
        ){
            val intent = Intent(context, HomeActivity::class.java).apply{
                putExtra(USER_INFO, user)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI(){
        moviesFragment = MoviesFragment()
        charactersFragment = CharactersFragment()
        libraryFragment = LibraryFragment()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        // Al navegar hacia la pantalla de detalles tanto de la película como la del personaje,
        // no se mostrará el bottom navigation bar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(
                destination.id == R.id.movieDetailFragment ||
                destination.id == R.id.characterDetailFragment ||
                destination.id == R.id.commentsFragment
            ) {
                binding.toolbar.menu.clear()
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.toolbar.visibility = View.VISIBLE
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }

    override fun onMovieClick(movie: Movie) {
        val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailFragment(movie)
        navController.navigate(action)
    }

    override fun onCharacterClick(character: Character) {
        val action = CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(character)
        navController.navigate(action)
    }

    override fun onCommentClick(movie: Movie) {
        val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToCommentsFragment(movie)
        navController.navigate(action)
    }

    // TODO: revisar por si hay que eliminar esto
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {
                // TODO: Que lleve al perfil
            }
            R.id.nav_settings -> {
                // TODO: go to settings fragment
            }
            R.id.nav_logout -> {
                // Clearing session data
                lifecycleScope.launch { UserManager.clearData(applicationContext) }

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}