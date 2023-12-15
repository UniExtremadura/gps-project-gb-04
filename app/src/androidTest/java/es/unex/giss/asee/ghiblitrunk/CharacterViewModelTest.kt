package es.unex.giss.asee.ghiblitrunk

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.view.home.CharacterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharacterViewModelTest {

    private lateinit var context: Context
    private lateinit var repository: Repository
    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var character: Character

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        repository = TestRepository(context)
        characterViewModel = CharacterViewModel(repository)

        character = Character().apply {
            name = "Name"
            gender = "Gender"
            age = "Age"
            eye_color = "Eye color"
            hair_color = "Hair color"
        }

    }

    @Test
    fun testOnToastShown() = runBlocking {
        characterViewModel.onToastShown()

        delay(100)

        assertEquals(characterViewModel.toast.value, null)
    }

    @Test
    fun testSetCharacterDetail() = runBlocking {
        characterViewModel.setCharacterDetail(character)

        delay(100)

        assertEquals(characterViewModel.characterDetail.value, character)
    }

}