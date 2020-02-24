package kz.coffee.go.domain

import kz.coffee.go.data.IUsersRepo
import kz.coffee.go.utils.Resource

class IUsersImpl(private val usersRepo: IUsersRepo) : IUsers {

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<Boolean> = usersRepo.signInWithEmailAndPassword(email, password)

    override suspend fun signOut(): Resource<Boolean> = usersRepo.signOut()

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

    override suspend fun changeEmail(password: String, newEmail: String): Resource<Boolean> =
        usersRepo.changeEmail(password, newEmail)

    override suspend fun changeUserData(user: User): Resource<Boolean> =
        usersRepo.changeUserData(user)

}