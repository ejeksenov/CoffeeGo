package kz.coffee.go.data

import kz.coffee.go.domain.User
import kz.coffee.go.utils.Resource

interface IUsersRepo {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<Boolean>
    suspend fun signOut(): Resource<Boolean>
    suspend fun signUpWithEmailAndPassword(user: User, email: String, password: String): Resource<Boolean>
    suspend fun getUserById(): Resource<User>
    suspend fun sendResetPassword(email: String): Resource<Boolean>
    suspend fun changePassword(password: String, newPassword: String): Resource<Boolean>
    suspend fun changeEmail(password: String, newEmail: String): Resource<Boolean>
    suspend fun changeUserData(user: User): Resource<Boolean>
}