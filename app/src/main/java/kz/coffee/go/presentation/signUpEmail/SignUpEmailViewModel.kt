package kz.coffee.go.presentation.signUpEmail

import androidx.lifecycle.liveData
import kz.coffee.go.domain.user.IUsers
import kz.coffee.go.domain.user.User
import kz.coffee.go.presentation.base.BaseViewModel
import kz.coffee.go.utils.Resource
import java.lang.Exception

class SignUpEmailViewModel(private val userUseCase: IUsers) : BaseViewModel() {

    fun createUserWithEmailAndPassword(email: String, password: String, fullName: String) =
        liveData(coroutineContext.io) {
            emit(Resource.Loading())
            try {
                val user = User(
                    fullName = fullName,
                    cashback = 0.0,
                    city = "",
                    imageUrl = ""
                )
                val isUserCreated = userUseCase.signUpWithEmailAndPassword(user, email, password)
                emit(isUserCreated)
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

}
