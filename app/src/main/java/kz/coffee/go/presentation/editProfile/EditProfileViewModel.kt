package kz.coffee.go.presentation.editProfile

import android.net.Uri
import androidx.lifecycle.liveData
import kz.coffee.go.domain.IUsers
import kz.coffee.go.domain.User
import kz.coffee.go.presentation.base.BaseViewModel
import kz.coffee.go.utils.Resource
import java.lang.Exception

class EditProfileViewModel(private val useCase: IUsers) : BaseViewModel() {

    fun getUserData() = liveData(coroutineContext.io){
        emit(Resource.Loading())
        try {
            val userData = useCase.getUserById()
            emit(userData)
        }catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun editProfile(user: User) = liveData(coroutineContext.io) {
        emit(Resource.Loading())
        try {
            val isUserDataEdited = useCase.changeUserData(user)
            emit(isUserDataEdited)
        }catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun saveImageToStorage(uri: Uri) = liveData(coroutineContext.io) {
        emit(Resource.Loading())
        try {
            val imageUrl = useCase.saveAvatarToStorage(uri)
            emit(imageUrl)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

}
