package es.unex.giss.asee.ghiblitrunk

import android.content.Context
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.view.home.MovieViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import androidx.test.core.app.ApplicationProvider
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Assert.assertNotEquals

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    private lateinit var context: Context
    private lateinit var repository: Repository
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movie: Movie

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        repository = TestRepository(context)
        movieViewModel = MovieViewModel(repository)

        movie = Movie().apply {
            title = "Title"
            description = "Description"
            director = "Director"
            isFavourite = false
            producer = "Producer"
            release_date = "Release Date"
        }
    }

    @Test
    fun testSetSearchFilter() {
        val filter = "Search by Title"

        movieViewModel.setSearchFilter(filter)

        assertEquals(filter, "Search by Title")
    }

    @Test
    fun testOnToastShown() {
        // When
        CoroutineScope(Dispatchers.IO).launch {
            movieViewModel.onToastShown()

            assertEquals(movieViewModel.toast.value, null)
        }
    }

    @Test
    fun testFetchMovie(){
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)

            assertNotEquals(movieViewModel.movieDetail.value, null)
        }
    }

    @Test
    fun testSetMovieDetail(){
        CoroutineScope(Dispatchers.IO).launch {
            movieViewModel.setMovieDetail(movie)

            delay(100)
            assertEquals(movieViewModel.movieDetail.value, movie)
        }
    }

}
