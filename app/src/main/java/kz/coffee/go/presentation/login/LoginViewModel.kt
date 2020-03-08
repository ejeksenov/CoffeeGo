package kz.coffee.go.presentation.login

import androidx.lifecycle.*
import kz.coffee.go.domain.user.IUsers
import kz.coffee.go.presentation.base.BaseViewModel
import kz.coffee.go.utils.Resource
import java.lang.Exception

class LoginViewModel(private val userUseCase: IUsers) : BaseViewModel() {


    fun signInWithEmail(email: String, password: String) = liveData(coroutineContext.io) {
        emit(Resource.Loading())
        try {
            val isSignedIn = userUseCase.signInWithEmailAndPassword(email, password)
            emit(isSignedIn)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}
