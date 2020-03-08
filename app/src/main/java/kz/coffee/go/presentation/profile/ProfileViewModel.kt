package kz.coffee.go.presentation.profile

import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import kz.coffee.go.domain.user.IUsers
import kz.coffee.go.presentation.base.BaseViewModel
import kz.coffee.go.utils.Resource
import java.lang.Exception

class ProfileViewModel(private val useCase: IUsers) : BaseViewModel() {

    fun getUsersData() = liveData(coroutineContext.io) {
        emit(Resource.Loading())
        try {
            val user = useCase.getUserById()
            emit(user)
        }catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}
