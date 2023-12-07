package es.unex.giss.asee.ghiblitrunk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.unex.giss.asee.ghiblitrunk.data.models.User
import es.unex.giss.asee.ghiblitrunk.databinding.ActivityWelcomeBinding
import es.unex.giss.asee.ghiblitrunk.view.home.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {

    private val FINISH_TIME_MILIS : Long = 2000

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var currentUser: User

    companion object {
        const val USER_INFO = "USER_INFO"
        fun start(
            context: Context,
            user: User
        ){
            val intent = Intent(context, WelcomeActivity::class.java).apply{
                putExtra(USER_INFO, user)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = intent.getSerializableExtra(USER_INFO) as User

        with(binding){
            appName.text = "Ghibli Trunk"
            welcome.text = "Welcome, ${currentUser.name}"
        }

        lifecycleScope.launch {
            delay(FINISH_TIME_MILIS)
            startHomeActivity()
        }

    }

    private fun startHomeActivity() {
        HomeActivity.start(this, currentUser)
    }
}
