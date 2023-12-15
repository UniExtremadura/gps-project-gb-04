package es.unex.giss.asee.ghiblitrunk.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.unex.giss.asee.ghiblitrunk.GhibliTrunkApplication
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.User
import es.unex.giss.asee.ghiblitrunk.databinding.ActivityJoinBinding
import es.unex.giss.asee.ghiblitrunk.utils.CredentialCheck
import kotlinx.coroutines.launch

class JoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding
    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding and set content view
        binding = ActivityJoinBinding.inflate(layoutInflater) //se establece el layout de nuestra activity
        setContentView(binding.root)

        val appContainer = (this.application as GhibliTrunkApplication).appContainer
        repository = appContainer.repository

        setUpListeners()
    }

    private fun setUpListeners() {
        with(binding) {
            //Sign Up Button
            btSign.setOnClickListener { joinUser() }

        }

    }

    private fun navigateToHomeActivity(user: User) {
        WelcomeActivity.start(this, user)
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun joinUser() {
        with(binding) {
            val check = CredentialCheck.join(
                etFirstname.text.toString(),
                etLastName.text.toString(),
                etUser.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString(),
                etRepeatpassword.text.toString()
            )
            if (check.fail) notifyInvalidCredentials(check.msg)
            else {
                val user = User(null, etUser.text.toString(), etPassword.text.toString())
                lifecycleScope.launch{
                    val userId = repository.insertUser(user)
                    user.userId = userId
                    navigateToHomeActivity(user)
                }
            }
        }
    }

}