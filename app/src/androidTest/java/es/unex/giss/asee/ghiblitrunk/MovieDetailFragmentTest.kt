import android.content.Context
import androidx.test.core.app.ApplicationProvider
import es.unex.giss.asee.ghiblitrunk.TestRepository
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.view.home.MovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MovieDetailFragmentTest {

    private lateinit var context: Context
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var repository: Repository

    private lateinit var movie: Movie

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = TestRepository(context)
        movieViewModel = MovieViewModel(repository)

        movie = Movie().apply {
            title = "Test Movie"
            original_title = "Test Original Title"
            description = "Test Description"
            director = "Test Director"
            producer = "Test Producer"
            release_date = "Test Release Date"
        }

        movieViewModel.setMovieDetail(movie)
    }

    @Test
    fun testMovieTitle() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            assertEquals("Test Movie", movieViewModel.movieDetail.value?.title)
        }
    }

    @Test
    fun testMovieOriginalTitle() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            assertEquals("Test Original Title", movieViewModel.movieDetail.value?.original_title)
        }
    }

    @Test
    fun testMovieDescription() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            assertEquals("Test Description", movieViewModel.movieDetail.value?.description)
        }
    }

    @Test
    fun testMovieDirector() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            assertEquals("Director: Test Director", movieViewModel.movieDetail.value?.director)
        }
    }

    @Test
    fun testMovieProducer() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            assertEquals("Producer: Test Producer", movieViewModel.movieDetail.value?.producer)
        }
    }

    @Test
    fun testMovieReleaseDate() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            assertEquals("Release date: Test Release Date", movieViewModel.movieDetail.value?.release_date)
        }
    }


}
