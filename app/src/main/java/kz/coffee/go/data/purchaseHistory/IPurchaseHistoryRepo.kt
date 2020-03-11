package kz.coffee.go.data.purchaseHistory

import kz.coffee.go.domain.purchaseHistory.PurchaseHistory
import kz.coffee.go.utils.Resource

interface IPurchaseHistoryRepo {
    suspend fun savePurchaseHistory(purchaseHistory: PurchaseHistory, cafeteriaId: String): Resource<Boolean>
}