package es.unex.giss.asee.ghiblitrunk

import android.content.Context
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.view.home.MovieViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    private lateinit var context: Context
    private lateinit var repository: Repository
    private lateinit var movieViewModel: MovieViewModel

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        repository = TestRepository(context)
        movieViewModel = MovieViewModel(repository)
    }

    @Test
    fun testSetSearchFilter() {
        // Given
        val filter = "Search by Title"

        // When
        movieViewModel.setSearchFilter(filter)

        // Then
        assertEquals(filter, "Search by Title")
    }

    @Test
    fun testOnToastShown() {
        // When
        CoroutineScope(Dispatchers.IO).launch {
            movieViewModel.onToastShown()
        }

        // Then
        assertEquals(movieViewModel.toast.value, null)
    }

}
