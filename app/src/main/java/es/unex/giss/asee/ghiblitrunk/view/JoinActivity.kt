package es.unex.giss.asee.ghiblitrunk.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.unex.giss.asee.ghiblitrunk.data.models.User
import es.unex.giss.asee.ghiblitrunk.database.GhibliTrunkDatabase
import es.unex.giss.asee.ghiblitrunk.databinding.ActivityJoinBinding
import es.unex.giss.asee.ghiblitrunk.utils.CredentialCheck
import kotlinx.coroutines.launch

class JoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding
    private lateinit var db: GhibliTrunkDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding and set content view
        binding = ActivityJoinBinding.inflate(layoutInflater) //se establece el layout de nuestra activity
        setContentView(binding.root)

        //database instance reference
        db = GhibliTrunkDatabase.getInstance(applicationContext)!!

        //views initialization and listeners
        setUpUI()
        setUpListeners()
    }

    private fun setUpUI() {
        //get attributes from xml using binding
    }

    private fun setUpListeners() {
        with(binding) {
            //Sign Up Button
            btSign.setOnClickListener { joinUser() }

        }

    }

    private fun navigateToHomeActivity(user: User) {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.putExtra("SIGNUP_USER", user)
        startActivity(intent)
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun joinUser() {
        with(binding) {
            val check = CredentialCheck.join(
                etFirstname.text.toString(),
                etSecondname.text.toString(),
                etUser.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString(),
                etRepeatpassword.text.toString()
            )
            if (check.fail) notifyInvalidCredentials(check.msg)
            else {
                lifecycleScope.launch{
                    val user = User(
                        null,
                        etUser.text.toString(),
                        etPassword.text.toString()
                    )

                    db?.userDao()?.insert(user)

                    navigateToHomeActivity(
                        User(
                            null,
                            etUser.text.toString(),
                            etPassword.text.toString()
                        )
                    )
                }
            }
        }
    }

}