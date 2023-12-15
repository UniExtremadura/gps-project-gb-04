package es.unex.giss.asee.ghiblitrunk

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var context: Context
    private lateinit var repository: Repository
    private lateinit var movie: Movie
    private lateinit var character: Character

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        repository = TestRepository(context)

        movie = Movie().apply {
            title = "Title"
            description = "Description"
            director = "Director"
            isFavourite = false
            producer = "Producer"
            release_date = "Release Date"
        }

        character = Character().apply {
            name = "Name"
            gender = "Gender"
            age = "Age"
            eye_color = "Eye color"
            hair_color = "Hair color"
            isFavourite = false
        }
    }

    @Test
    fun testFavoriteMovie() = runBlocking {
        repository.insertMovie(movie)
        repository.movieToLibrary(movie, 1)

        assertEquals(repository.getIfFavorite(movie), true)
        repository.deleteMovieFromLibrary(movie, 1)
        assertEquals(repository.getIfFavorite(movie), false)

        repository.deleteMovie(movie)
    }

    @Test
    fun testFavoriteCharacter() = runBlocking {
        repository.insertCharacter(character)
        repository.characterToLibrary(character, 1)

        assertEquals(repository.getIfFavorite(character), true)
        repository.deleteCharacterFromLibrary(character, 1)
        assertEquals(repository.getIfFavorite(character), false)

        repository.deleteCharacter(character)
    }

    @Test
    fun testInitialGetFavouriteCharacters() = runBlocking {
        assertEquals(repository.getFavoritesCharacters(), emptyList<Character>())
    }

    @Test
    fun testInitialGetFavouriteMovie() = runBlocking {
        assertEquals(repository.getFavoritesMovies(), emptyList<Movie>())
    }

    @Test
    fun testGetFavouriteCharacter() = runBlocking {
        repository.insertCharacter(character)
        repository.characterToLibrary(character, 1)

        assertEquals(repository.getFavoritesCharacters(), List<Character>(1){character})

        repository.deleteCharacter(character)
    }

    @Before
    fun clearTestData(){

    }

}
