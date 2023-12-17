package es.unex.giss.asee.ghiblitrunk.view.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import es.unex.giss.asee.ghiblitrunk.R
import es.unex.giss.asee.ghiblitrunk.data.models.User
import es.unex.giss.asee.ghiblitrunk.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val viewModel: ProfileViewModel by viewModels { ProfileViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.setUserInSession(intent.getSerializableExtra(HomeActivity.USER_INFO) as User)
        binding.tvUsername.text = viewModel.user.value?.name

        viewModel.user.observe(this){user ->
            binding.tvUsername.text = user.name
        }

        viewModel.toast.observe(this) {text ->
            text?.let {
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        setupListeners()
    }

    private fun setupListeners(){
        with(binding){

            // Cambiar contraseña
            btChangePassword.setOnClickListener {

                viewModel.onChangePasswordButtonClick(
                    etPassword.text.toString(),
                    etNewpassword.text.toString(),
                    etRepeatpassword.text.toString()
                )

                etPassword.text.clear()
                etNewpassword.text.clear()
                etRepeatpassword.text.clear()
            }

            // Cambiar nombre de usuario
            ivChangeUsername.setOnClickListener{
                showChangeUsernameDialog()
            }

        }
    }

    private fun showChangeUsernameDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_change_username, null)
        val editTextNewUsername = dialogView.findViewById<EditText>(R.id.editTextUsername)

        dialogBuilder.apply {
            setTitle("Change your username:")
            setView(dialogView)
            setPositiveButton("Change") { dialog, _ ->
                val newUsername = editTextNewUsername.text.toString()
                binding.tvUsername.text=newUsername
                viewModel.changeUsername(newUsername)
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        const val USER_INFO = "USER_INFO"
        fun start(
            context: Context,
            user: User
        ){
            val intent = Intent(context, ProfileActivity::class.java).apply{
                putExtra(USER_INFO, user)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

}