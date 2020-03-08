package kz.coffee.go.domain.user

import android.net.Uri
import kz.coffee.go.data.user.IUsersRepo
import kz.coffee.go.utils.Resource

class IUsersImpl(private val usersRepo: IUsersRepo) :
    IUsers {

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<Boolean> = usersRepo.signInWithEmailAndPassword(email, password)

    override suspend fun signUpWithEmailAndPassword(
        user: User,
        email: String,
        password: String
    ): Resource<Boolean> = usersRepo.signUpWithEmailAndPassword(user, email, password)

    override suspend fun getUserById(): Resource<User> = usersRepo.getUserById()

    override suspend fun sendResetPassword(email: String): Resource<Boolean> =
        usersRepo.sendResetPassword(email)

    override suspend fun changePassword(password: String, newPassword: String): Resource<Boolean> =
        usersRepo.changePassword(password, newPassword)

    override suspend fun reauthenticateUser(password: String): Resource<Boolean> {
        return usersRepo.reauthenticateUser(password)
    }

    override suspend fun changeEmail(newEmail: String): Resource<Boolean> {
        return usersRepo.changeEmail(newEmail)
    }

    override suspend fun changeUserData(user: User): Resource<Boolean> =
        usersRepo.changeUserData(user)

    override suspend fun saveAvatarToStorage(uri: Uri): Resource<String> {
        return usersRepo.saveAvatarToStorage(uri)
    }

}