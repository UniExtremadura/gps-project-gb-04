import android.content.Context
import androidx.test.core.app.ApplicationProvider
import es.unex.giss.asee.ghiblitrunk.TestRepository
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.view.home.MovieViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MovieDetailViewModelTest {

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
    fun testMovieTitle() = runBlocking {
        delay(100)
        assertEquals("Test Movie", movieViewModel.movieDetail.value?.title)
    }

    @Test
    fun testMovieOriginalTitle()  = runBlocking {
        delay(100)
        assertEquals("Test Original Title", movieViewModel.movieDetail.value?.original_title)
    }

    @Test
    fun testMovieDescription() = runBlocking {
        delay(100)
        assertEquals("Test Description", movieViewModel.movieDetail.value?.description)
    }

    @Test
    fun testMovieDirector() = runBlocking {
        delay(100)
        assertEquals("Test Director", movieViewModel.movieDetail.value?.director)
    }

    @Test
    fun testMovieProducer() = runBlocking {
        delay(100)
        assertEquals("Test Producer", movieViewModel.movieDetail.value?.producer)
    }

    @Test
    fun testMovieReleaseDate() = runBlocking {
        delay(100)
        assertEquals("Test Release Date", movieViewModel.movieDetail.value?.release_date)
    }


}
