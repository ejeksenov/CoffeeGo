package kz.coffee.go.data.purchaseHistory

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kz.coffee.go.domain.cafeteria.cafeteriasCollectionName
import kz.coffee.go.domain.purchaseHistory.PurchaseHistory
import kz.coffee.go.domain.purchaseHistory.purchaseHistoryTree
import kz.coffee.go.domain.user.userCollectionName
import kz.coffee.go.utils.Resource

class PurchaseHistoryRepoImpl : IPurchaseHistoryRepo {
    private val mAuth = FirebaseAuth.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    private var resource: Resource<Boolean>? = null

    override suspend fun savePurchaseHistory(
        purchaseHistory: PurchaseHistory,
        cafeteriaId: String
    ): Resource<Boolean> {
        val mUserId = mAuth.currentUser?.uid!!
        val userDocRef = mFirestore.collection(userCollectionName).document(mUserId)
        val cafeteriaDocRef = mFirestore.collection(cafeteriasCollectionName).document(cafeteriaId)
        val timeStamp: Timestamp = Timestamp.now()
        purchaseHistory.userId = userDocRef
        purchaseHistory.cafeteriaId = cafeteriaDocRef
        purchaseHistory.time = timeStamp
        mFirestore.collection(purchaseHistoryTree).document().set(purchaseHistory)
            .addOnCompleteListener {
                resource =
                    if (it.isSuccessful) Resource.Success(true) else Resource.Failure(it.exception!!)
            }.await()
        return resource!!
    }
}