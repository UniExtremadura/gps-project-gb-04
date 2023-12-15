package es.unex.giss.asee.ghiblitrunk

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.User
import es.unex.giss.asee.ghiblitrunk.view.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    private lateinit var context: Context
    private lateinit var repository: Repository
    private lateinit var user: User

    private val testUserName = "user"
    private val testUserPassword = "password"

    @Before
    fun setUp(){
        context = ApplicationProvider.getApplicationContext()
        repository = TestRepository(context)

        user = User(
            userId = 1000000,
            name = testUserName,
            password = testUserPassword
        )

        CoroutineScope(Dispatchers.IO).launch {
            repository.insertUser(user)
        }
    }

    @Test
    fun testLoginSuccess() {
        // Simular entrada de texto en los campos de usuario y contraseña
        Espresso.onView(ViewMatchers.withId(R.id.et_username))
            .perform(ViewActions.typeText(testUserName))

        Espresso.onView(ViewMatchers.withId(R.id.et_password))
            .perform(ViewActions.typeText(testUserPassword))

        // Cerrar el teclado virtual
        Espresso.closeSoftKeyboard()

        // Hacer clic en el botón de inicio de sesión
        Espresso.onView(ViewMatchers.withId(R.id.bt_login))
            .perform(ViewActions.click())

        // Verificar que se navega correctamente a la actividad de bienvenida
        Espresso.onView(ViewMatchers.withId(R.id.imageViewBackground))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testNavigationToJoinActivity() {
        // Hacer clic en el enlace para registrarse
        Espresso.onView(ViewMatchers.withId(R.id.tv_join))
            .perform(ViewActions.click())

        // Verificar que se navega correctamente a la actividad de registro
        Espresso.onView(ViewMatchers.withId(R.id.tv_joinTitle))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}