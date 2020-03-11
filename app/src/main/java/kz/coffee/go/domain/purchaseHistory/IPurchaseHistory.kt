package kz.coffee.go.domain.purchaseHistory

import kz.coffee.go.utils.Resource

interface IPurchaseHistory {
    suspend fun savePurchaseHistory(purchaseHistory: PurchaseHistory, cafeteriaId: String): Resource<Boolean>
}