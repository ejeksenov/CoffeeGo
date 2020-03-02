package kz.coffee.go.data.user

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kz.coffee.go.data.user.IUsersRepo
import kz.coffee.go.domain.User
import kz.coffee.go.domain.userCollectionName
import kz.coffee.go.utils.Resource

class UsersRepoImpl : IUsersRepo {

    private val mAuth = FirebaseAuth.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    private var resource: Resource<Boolean>? = null

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<Boolean> {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            resource = if (it.isSuccessful)
                Resource.Success(true)
            else
                Resource.Failure(it.exception!!)
        }
        return resource!!
    }

    override suspend fun signOut(): Resource<Boolean> {
        mAuth.signOut()
        return Resource.Success(true)
    }

    override suspend fun signUpWithEmailAndPassword(
        user: User,
        email: String,
        password: String
    ): Resource<Boolean> {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val mUser = mAuth.currentUser
                if (mUser != null)
                    mFirestore.collection(userCollectionName).document(mUser.uid).set(user).addOnCompleteListener { it1 ->
                        resource = if (it1.isSuccessful)
                            Resource.Success(true)
                        else
                            Resource.Failure(it.exception!!)
                    }
            } else
                resource = Resource.Failure(it.exception!!)

        }
        return resource!!
    }

    override suspend fun getUserById(): Resource<User> {
        val mUser = mAuth.currentUser
        val mUserData =
            mFirestore.collection(userCollectionName).document(mUser?.uid!!).get().await()
        val user = User(
            fullName = mUserData.getString("fullName"),
            cashback = mUserData.getDouble("cashback"),
            city = mUserData.getString("city"),
            imageUrl = mUserData.getString("imageUrl")
        )
        return Resource.Success(user)
    }

    override suspend fun sendResetPassword(email: String): Resource<Boolean> {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            resource =
                if (it.isSuccessful) Resource.Success(true)
                else Resource.Failure(it.exception!!)
        }
        return resource!!
    }

    override suspend fun changePassword(password: String, newPassword: String): Resource<Boolean> {
        val currentUser = mAuth.currentUser
        val email = currentUser?.email
        if (currentUser != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, password)
            currentUser.reauthenticate(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    currentUser.updatePassword(newPassword).addOnCompleteListener {it1 ->
                        resource = if (it1.isSuccessful)
                            Resource.Success(true)
                        else
                            Resource.Failure(it.exception!!)
                    }
                } else
                    resource = Resource.Failure(it.exception!!)
            }
        }
        return resource!!
    }

    override suspend fun changeEmail(password: String, newEmail: String): Resource<Boolean> {
        val currentUser = mAuth.currentUser
        val email = currentUser?.email
        if (currentUser != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, password)
            currentUser.reauthenticate(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    currentUser.updateEmail(newEmail).addOnCompleteListener {it1 ->
                        resource = if (it1.isSuccessful)
                            Resource.Success(true)
                        else
                            Resource.Failure(it.exception!!)
                    }
                } else
                    resource = Resource.Failure(it.exception!!)
            }
        }
        return resource!!
    }

    override suspend fun changeUserData(user: User): Resource<Boolean> {
        val currentUser = mAuth.currentUser
        mFirestore.collection(userCollectionName).document(currentUser?.uid!!).set(user).addOnCompleteListener {
            resource = if (it.isSuccessful)
                Resource.Success(true)
            else
                Resource.Failure(it.exception!!)
        }
        return resource!!
    }

}