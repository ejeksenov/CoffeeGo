package kz.coffee.go.data.user

import android.net.Uri
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import kz.coffee.go.domain.user.User
import kz.coffee.go.domain.user.userCollectionName
import kz.coffee.go.utils.Resource

class UsersRepoImpl : IUsersRepo {

    private val mAuth = FirebaseAuth.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    private var resource: Resource<Boolean>? = null
    private var resourceData: Resource<String>? = null
    private val storageRef = FirebaseStorage.getInstance().reference

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<Boolean> {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            resource =
                if (it.isSuccessful) Resource.Success(true) else Resource.Failure(it.exception!!)
        }.await()
        return resource!!
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
                    mFirestore.collection(userCollectionName).document(mUser.uid).set(user)
                        .addOnCompleteListener { it1 ->
                            resource = if (it1.isSuccessful)
                                Resource.Success(true)
                            else
                                Resource.Failure(it1.exception!!)
                        }
            } else
                resource = Resource.Failure(it.exception!!)

        }.await()
        return resource!!
    }

    override suspend fun getUserById(): Resource<User> {
        val mUser = mAuth.currentUser

        val mUserId = mUser?.uid
        val mUserData =
            mFirestore.collection(userCollectionName).document(mUserId!!).get().await()
        val user = User(
            cashback = mUserData.getDouble("cashback"),
            city = mUserData.getString("city"),
            fullName = mUserData.getString("fullName"),
            imageUrl = mUserData.getString("imageUrl")
        )
        return Resource.Success(user)

    }

    override suspend fun sendResetPassword(email: String): Resource<Boolean> {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            resource =
                if (it.isSuccessful) Resource.Success(true)
                else Resource.Failure(it.exception!!)
        }.await()
        return resource!!
    }

    override suspend fun changePassword(password: String, newPassword: String): Resource<Boolean> {
        val currentUser = mAuth.currentUser
        currentUser?.updatePassword(newPassword)?.addOnCompleteListener {
            resource = if (it.isSuccessful)
                Resource.Success(true)
            else
                Resource.Failure(it.exception!!)
        }?.await()
        return resource!!
    }

    override suspend fun reauthenticateUser(password: String): Resource<Boolean> {
        val currentUser = mAuth.currentUser
        val email = currentUser?.email
        val credential = EmailAuthProvider.getCredential(email!!, password)
        currentUser.reauthenticate(credential).addOnCompleteListener {
            resource =
                if (it.isSuccessful) Resource.Success(true) else Resource.Failure(it.exception!!)
        }.await()
        return resource!!
    }

    override suspend fun changeEmail(newEmail: String): Resource<Boolean> {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.updateEmail(newEmail)?.addOnCompleteListener {
            resource =
                if (it.isSuccessful) Resource.Success(true) else Resource.Failure(it.exception!!)
        }?.await()
        return resource!!
    }

    override suspend fun changeUserData(user: User): Resource<Boolean> {
        val currentUser = mAuth.currentUser
        mFirestore.collection(userCollectionName).document(currentUser?.uid!!).set(user)
            .addOnCompleteListener {
                resource = if (it.isSuccessful)
                    Resource.Success(true)
                else
                    Resource.Failure(it.exception!!)
            }.await()
        return resource!!
    }

    override suspend fun saveAvatarToStorage(uri: Uri): Resource<String> {
        val mUser = mAuth.currentUser
        val mUserId = mUser?.uid
        val profileImageRef: StorageReference = storageRef.child("avatar/$mUserId/1.jpg")
        val uploadTask = profileImageRef.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    resourceData = Resource.Failure(it)
                    throw it
                }
            }
            profileImageRef.downloadUrl
        }.addOnCompleteListener { task ->
            resourceData = if (task.isSuccessful) {
                val downloadUri = task.result.toString()
                Resource.Success(downloadUri)
            } else {
                Resource.Failure(task.exception!!)
            }
        }.await()

        return resourceData!!
    }


}