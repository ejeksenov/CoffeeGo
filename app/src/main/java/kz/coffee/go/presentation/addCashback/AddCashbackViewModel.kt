package kz.coffee.go.presentation.addCashback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kz.coffee.go.domain.purchaseHistory.IPurchaseHistory
import kz.coffee.go.domain.purchaseHistory.PurchaseHistory
import kz.coffee.go.domain.user.IUsers
import kz.coffee.go.domain.user.User
import kz.coffee.go.presentation.base.BaseViewModel
import kz.coffee.go.utils.Resource
import java.lang.Exception

class AddCashbackViewModel(private val useCase: IUsers, private val purchaseUseCase: IPurchaseHistory) : BaseViewModel() {

    fun getUserById() = liveData(coroutineContext.io) {
        emit(Resource.Loading())
        try {
            val user = useCase.getUserById()
            emit(user)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun saveUserData(user: User) = liveData(coroutineContext.io) {
        emit(Resource.Loading())
        try {
            val isUserDataSaved = useCase.changeUserData(user)
            emit(isUserDataSaved)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun savePurchaseHistory(purchaseHistory: PurchaseHistory, cafeteriaId: String) = liveData(coroutineContext.io) {
        emit(Resource.Loading())
        try {
            val isPurchaseHistorySaved = purchaseUseCase.savePurchaseHistory(purchaseHistory, cafeteriaId)
            emit(isPurchaseHistorySaved)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

}
