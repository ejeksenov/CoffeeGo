package kz.coffee.go.presentation.congratulations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kz.coffee.go.domain.cafeteria.ICafeterias
import kz.coffee.go.presentation.base.BaseViewModel
import kz.coffee.go.utils.Resource
import java.lang.Exception

class CongratulationsViewModel(private val useCase: ICafeterias) : BaseViewModel() {

    fun getCafeteriaById(cafeteriaId: String) = liveData(coroutineContext.io) {
        emit(Resource.Loading())
        try {
            val cafeteria = useCase.getCafeteriaById(cafeteriaId)
            emit(cafeteria)
        }catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}
