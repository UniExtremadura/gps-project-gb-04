package es.unex.giss.asee.ghiblitrunk

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import es.unex.giss.asee.ghiblitrunk.view.JoinActivity
import org.junit.Rule
import org.junit.Test

class JoinActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(JoinActivity::class.java)

    @Test
    fun testJoinSuccess() {
        // Simular entrada de texto en los campos de registro
        Espresso.onView(ViewMatchers.withId(R.id.et_firstname))
            .perform(ViewActions.typeText("John"))

        Espresso.onView(ViewMatchers.withId(R.id.et_lastName))
            .perform(ViewActions.typeText("Doe"))

        Espresso.onView(ViewMatchers.withId(R.id.et_user))
            .perform(ViewActions.typeText("johnDoe"))

        Espresso.onView(ViewMatchers.withId(R.id.et_email))
            .perform(ViewActions.typeText("johnDoe@gmail.com"))

        Espresso.onView(ViewMatchers.withId(R.id.et_password))
            .perform(ViewActions.typeText("JohnDoe1"))

        Espresso.onView(ViewMatchers.withId(R.id.et_repeatpassword))
            .perform(ViewActions.typeText("JohnDoe1"))

        // Cerrar el teclado virtual
        Espresso.closeSoftKeyboard()

        // Marcar la casilla de términos y condiciones
        Espresso.onView(ViewMatchers.withId(R.id.et_conditions))
            .perform(ViewActions.click())

        // Hacer clic en el botón de registro
        Espresso.onView(ViewMatchers.withId(R.id.bt_sign))
            .perform(ViewActions.click())

        // Verificar que se navega correctamente a la actividad de bienvenida
        Espresso.onView(ViewMatchers.withId(R.id.imageViewBackground)) // Reemplaza con la ID correcta de la WelcomeActivity
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}