package es.unex.giss.asee.ghiblitrunk

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.view.home.CharacterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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
    fun testFetchCharacter(){
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)

            assertNotEquals(characterViewModel.characterDetail.value, null)
        }
    }

    @Test
    fun testOnToastShown() {
        CoroutineScope(Dispatchers.IO).launch {
            characterViewModel.onToastShown()
            assertEquals(characterViewModel.toast.value, null)
        }
    }

    @Test
    fun testSetCharacterDetail(){
        CoroutineScope(Dispatchers.IO).launch {
            characterViewModel.setCharacterDetail(character)

            delay(100)
            assertEquals(characterViewModel.characterDetail.value, character)
        }
    }

}