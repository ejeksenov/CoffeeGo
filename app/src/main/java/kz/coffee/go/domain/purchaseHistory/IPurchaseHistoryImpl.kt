package kz.coffee.go.domain.purchaseHistory

import kz.coffee.go.data.purchaseHistory.IPurchaseHistoryRepo
import kz.coffee.go.utils.Resource

class IPurchaseHistoryImpl(private val iPurchaseHistoryRepo: IPurchaseHistoryRepo): IPurchaseHistory {
    override suspend fun savePurchaseHistory(
        purchaseHistory: PurchaseHistory,
        cafeteriaId: String
    ): Resource<Boolean> {
        return iPurchaseHistoryRepo.savePurchaseHistory(purchaseHistory, cafeteriaId)
    }
}