package kz.coffee.go.presentation.changeEmail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import kz.coffee.go.domain.IUsers
import kz.coffee.go.presentation.base.BaseViewModel
import kz.coffee.go.utils.Resource
import java.lang.Exception

class ChangeEmailViewModel(private val useCase: IUsers) : BaseViewModel() {

    fun reauthenticateUser(password: String) = liveData(coroutineContext.io) {
        emit(Resource.Loading())
        try {
            val isUserReauthenticated = useCase.reauthenticateUser(password)
            emit(isUserReauthenticated)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun changeEmail(email: String) = liveData(coroutineContext.io){
        emit(Resource.Loading())
        try {
            val isEmailChanged = useCase.changeEmail(email)
            emit(isEmailChanged)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    /*fun changeEmail(email: String, password: String) = liveData(coroutineContext.io){
        emit(Resource.Loading())
        try {
            val isEmailChanged = useCase.changeEmail(password, email)
            emit(isEmailChanged)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }*/

    fun getCurrentEmail(): String {
        val mAuth = FirebaseAuth.getInstance()
        val mUser = mAuth.currentUser
        if (mUser != null) {
            val email = mUser.email
            return email!!
        }
        return ""
    }
}
