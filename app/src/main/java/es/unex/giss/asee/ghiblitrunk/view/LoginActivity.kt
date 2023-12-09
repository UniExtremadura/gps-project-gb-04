package es.unex.giss.asee.ghiblitrunk.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.unex.giss.asee.ghiblitrunk.GhibliTrunkApplication
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.User
import es.unex.giss.asee.ghiblitrunk.databinding.ActivityLoginBinding
import es.unex.giss.asee.ghiblitrunk.utils.CredentialCheck
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var repository: Repository

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

        val appContainer = (this.application as GhibliTrunkApplication).appContainer
        repository = appContainer.repository

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
        WelcomeActivity.start(this, user)
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
                val trimmedUsername = binding.etUsername.text.toString().trim() // Elimina espacios en blanco al inicio y final
                val user = repository.findUser(trimmedUsername)
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
                // Ir a la pantalla principal
                navigateToHomeActivity(user, check.msg)
            }
        } else {
            notifyInvalidCredentials("Invalid username")
        }
    }
}
