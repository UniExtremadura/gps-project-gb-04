package es.unex.giss.asee.ghiblitrunk.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.unex.giss.asee.ghiblitrunk.data.models.User
import es.unex.giss.asee.ghiblitrunk.database.GhibliTrunkDatabase
import es.unex.giss.asee.ghiblitrunk.databinding.ActivityLoginBinding
import es.unex.giss.asee.ghiblitrunk.login.UserManager
import es.unex.giss.asee.ghiblitrunk.utils.CredentialCheck
import es.unex.giss.asee.ghiblitrunk.view.home.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val db: GhibliTrunkDatabase by lazy {
        GhibliTrunkDatabase.getInstance(applicationContext)!!
    }
    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                //TODO get data from result and update IU
                val name = ""
                val password = ""
                Toast.makeText(
                    this@LoginActivity,
                    "New user ($name/$password) created",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {  //el primer método de ciclo de vida de un activity es el onCreate()
        super.onCreate(savedInstanceState)

        //view binding and set content view
        binding = ActivityLoginBinding.inflate(layoutInflater) //se establece el layout de nuestra activity
        setContentView(binding.root)

        //configurar la interfaz de usuario, darle valores a las vistas que tenemos en la interfaz
        setUpUI()
        // Configurar listeners
        setUpListeners()
    }

    private fun setUpUI() {
        //get attributes from xml using binding
        //no se inicializa nuestras vistas de ninguna manera
    }

    private fun setUpListeners() {
        // With is used
        with(binding) {
            btLogin.setOnClickListener {checkLogin() }
            btJoin.setOnClickListener {navigateToJoinActivity() }
            btPasswordForget.setOnClickListener { navigateToForgetPasswordActivity() }
        }

    }

    private fun navigateToHomeActivity(user: User, msg: String) {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.putExtra(WelcomeActivity.LOGIN_USER, user)

        // Iniciar WelcomeActivity y cerrarla después de un breve retraso
        startActivity(intent)
        lifecycleScope.launch {
            delay(2000) // Espera 2 segundos
            finishWelcomeActivity()
        }
    }

    private fun finishWelcomeActivity() {
        val welcomeActivity = this@LoginActivity
        welcomeActivity.runOnUiThread {
            welcomeActivity.finish()
            // Iniciar MainPageActivity
            startActivity(Intent(welcomeActivity, HomeActivity::class.java))
        }
    }


    private fun navigateToForgetPasswordActivity(){
        val intent = Intent(this, PasswordForgetActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToJoinActivity() {
        val intent = Intent(this, JoinActivity::class.java)
        startActivity(intent)
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun checkLogin() {
        lifecycleScope.launch {
            try {
                val user = db.userDao().find(binding.etUsername.text.toString())
                handleLoginResult(user)
            } catch (e: Exception) {
                notifyInvalidCredentials("Error during login")
                Log.e("LoginActivity", "Error during login", e)
            }
        }
    }

    private fun handleLoginResult(user: User?) {
        if (user != null) {
            val check = CredentialCheck.passwordOk(binding.etPassword.text.toString(), user.password)
            if (check.fail) {
                notifyInvalidCredentials(check.msg)
            } else {
                // Guardar el usuario actual en el objeto y en SharedPreferences
                UserManager.saveCurrentUser(applicationContext, user)
                // Ir a la pantalla principal
                navigateToHomeActivity(user, check.msg)
            }
        } else {
            notifyInvalidCredentials("Invalid username")
        }
    }
}
