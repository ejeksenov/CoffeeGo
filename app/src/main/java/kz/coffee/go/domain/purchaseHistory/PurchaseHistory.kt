package kz.coffee.go.domain.purchaseHistory

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

const val purchaseHistoryTree = "PurchaseHistory"

data class PurchaseHistory(
    var cafeteriaId: DocumentReference?,
    var cashback: Double? = 0.0,
    var price: Double? = 0.0,
    var time: Timestamp?,
    var userId: DocumentReference?
)