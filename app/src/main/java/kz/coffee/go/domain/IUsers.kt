package kz.coffee.go.domain

import kz.coffee.go.utils.Resource

interface IUsers {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<Boolean>
    suspend fun signUpWithEmailAndPassword(user: User, email: String, password: String): Resource<Boolean>
    suspend fun getUserById(): Resource<User>
    suspend fun sendResetPassword(email: String): Resource<Boolean>
    suspend fun changePassword(password: String, newPassword: String): Resource<Boolean>
    suspend fun changeEmail(password: String, newEmail: String): Resource<Boolean>
    suspend fun changeUserData(user: User): Resource<Boolean>
}