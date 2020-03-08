package kz.coffee.go.domain.cafeteria

import kz.coffee.go.data.cafeteria.ICafeteriasRepo
import kz.coffee.go.utils.Resource

class ICafeteriasImpl(private val cafeteriasRepo: ICafeteriasRepo): ICafeterias {
    override suspend fun getCafeteriasByCity(city: String): Resource<List<Cafeteria>> {
        return cafeteriasRepo.getCafeteriasByCity(city)
    }
}