package es.unex.giss.asee.ghiblitrunk.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.unex.giss.asee.ghiblitrunk.data.models.User
import es.unex.giss.asee.ghiblitrunk.databinding.ActivityWelcomeBinding
import es.unex.giss.asee.ghiblitrunk.login.UserManager
import es.unex.giss.asee.ghiblitrunk.view.home.HomeActivity
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    companion object {
        val LOGIN_USER = "LOGIN_USER"
        const val DELAY_MILLIS = 3000L // Puedes ajustar el tiempo de retraso según sea necesario
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Restaurar el usuario actual desde SharedPreferences
        lifecycleScope.launch {
            var currentUser: User? = UserManager.loadCurrentUser(applicationContext)

            if (currentUser == null) {
                // No hay un usuario actual, ir a LoginActivity
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish() // Cerrar WelcomeActivity si está abierta
            } else {
                // Hay un usuario actual, mostrar mensaje de bienvenida y cerrar WelcomeActivity después del retraso
                with(binding){
                    appName.text = "Ghibli Trunk"
                    welcome.text = "Welcome, ${currentUser.name}"
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    finishWelcomeActivity()
                }, DELAY_MILLIS)
            }
        }
    }

    private fun finishWelcomeActivity() {
        val welcomeActivity = this@WelcomeActivity
        welcomeActivity.runOnUiThread {
            welcomeActivity.finish()
            // Iniciar HomeActivity
            startActivity(Intent(welcomeActivity, HomeActivity::class.java))
        }
    }
}
