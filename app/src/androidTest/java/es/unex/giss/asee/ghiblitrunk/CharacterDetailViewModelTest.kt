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
import org.junit.Before
import org.junit.Test

class CharacterDetailViewModelTest {

    private lateinit var context: Context
    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var repository: Repository
    private lateinit var character: Character

    @Before
    fun setUp() {
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
        characterViewModel.setCharacterDetail(character)
    }

    @Test
    fun testName() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            assertEquals("Name", characterViewModel.characterDetail.value?.name)
        }
    }

    @Test
    fun testGender() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            assertEquals("Gender", characterViewModel.characterDetail.value?.gender)
        }
    }

    @Test
    fun testAge() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            assertEquals("Age", characterViewModel.characterDetail.value?.age)
        }
    }

    @Test
    fun testEyeColor() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            assertEquals("Eye color", characterViewModel.characterDetail.value?.eye_color)
        }
    }

    @Test
    fun testHairColor() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            assertEquals("Hair color", characterViewModel.characterDetail.value?.hair_color)
        }
    }

}