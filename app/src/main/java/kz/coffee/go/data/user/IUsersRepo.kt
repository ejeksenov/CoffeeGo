package kz.coffee.go.data.user

import android.net.Uri
import kz.coffee.go.domain.user.User
import kz.coffee.go.utils.Resource

interface IUsersRepo {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<Boolean>
    suspend fun signUpWithEmailAndPassword(user: User, email: String, password: String): Resource<Boolean>
    suspend fun getUserById(): Resource<User>
    suspend fun sendResetPassword(email: String): Resource<Boolean>
    suspend fun changePassword(password: String, newPassword: String): Resource<Boolean>
    suspend fun reauthenticateUser(password: String): Resource<Boolean>
    suspend fun changeEmail(newEmail: String): Resource<Boolean>
    suspend fun changeUserData(user: User): Resource<Boolean>
    suspend fun saveAvatarToStorage(uri: Uri): Resource<String>
}