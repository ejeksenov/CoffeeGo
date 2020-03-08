package kz.coffee.go.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kz.coffee.go.domain.cafeteria.ICafeterias
import kz.coffee.go.presentation.base.BaseViewModel
import kz.coffee.go.utils.Resource
import java.lang.Exception

class HomeViewModel(private val useCase: ICafeterias): BaseViewModel() {

    fun getCafeteriasListByCity(city: String) = liveData(coroutineContext.io) {
        emit(Resource.Loading())
        try {
            val cafeteriasList = useCase.getCafeteriasByCity(city)
            emit(cafeteriasList)
        }catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}
