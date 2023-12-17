package es.unex.giss.asee.ghiblitrunk.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giss.asee.ghiblitrunk.GhibliTrunkApplication
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.data.models.User
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: Repository
): ViewModel() {
    private val _user = MutableLiveData<User>(null)
    val user: LiveData<User>
        get() = _user

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast



    fun onChangePasswordButtonClick(passField: String, newPassField: String, repassField: String): Boolean{
        return if(passField == user.value?.password){
            if(newPassField != repassField){
                setToastText("New password don't match")
                true
            } else{
                updateUserPassword(newPassField)
                setToastText("Password changed")
                false
            }
        }else{
            setToastText("Password don't match")
            false
        }
    }

    private fun updateUserPassword(newPassword: String) {
        viewModelScope.launch {
            repository.changeUserPassword(newPassword, user.value!!)
        }
    }

    fun changeUsername(newUsername: String){
        viewModelScope.launch {
            repository.changeUsername(newUsername, user.value!!)
        }
    }

    fun onToastShown(){
        viewModelScope.launch {
            _toast.value = null
        }
    }

    fun setUserInSession(user: User?){
        viewModelScope.launch(){
            _user.value = user!!
        }
    }

    private fun setToastText(text: String){
        viewModelScope.launch {
            _toast.value = text
        }
    }

    companion object{

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return ProfileViewModel(
                    (application as GhibliTrunkApplication).appContainer.repository,
                ) as T
            }
        }

    }
}