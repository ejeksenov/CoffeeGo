package kz.coffee.go.presentation.changePassword

import androidx.lifecycle.liveData
import kz.coffee.go.domain.user.IUsers
import kz.coffee.go.presentation.base.BaseViewModel
import kz.coffee.go.utils.Resource
import java.lang.Exception

class ChangePasswordViewModel(private val useCase: IUsers) : BaseViewModel() {

    fun reauthenticateUser(password: String) = liveData(coroutineContext.io) {
        emit(Resource.Loading())
        try {
            val isUserReauthenticated = useCase.reauthenticateUser(password)
            emit(isUserReauthenticated)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun changePassword(password: String, newPassword: String) = liveData(coroutineContext.io){
        emit(Resource.Loading())
        try {
            val isPasswordChanged = useCase.changePassword(password, newPassword)
            emit(isPasswordChanged)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

}
