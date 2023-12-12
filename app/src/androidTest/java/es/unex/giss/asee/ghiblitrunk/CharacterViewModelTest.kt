package es.unex.giss.asee.ghiblitrunk

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.view.home.CharacterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CharacterViewModelTest {

    private lateinit var context: Context
    private lateinit var repository: Repository
    private lateinit var characterViewModel: CharacterViewModel

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        repository = TestRepository(context)
        characterViewModel = CharacterViewModel(repository)
    }

    @Test
    fun testOnToastShown() {
        CoroutineScope(Dispatchers.IO).launch {
            characterViewModel.onToastShown()
            Assert.assertEquals(characterViewModel.toast.value, null)
        }
    }
}