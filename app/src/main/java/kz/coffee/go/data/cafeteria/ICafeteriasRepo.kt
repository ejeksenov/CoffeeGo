package kz.coffee.go.data.cafeteria

import kz.coffee.go.domain.cafeteria.Cafeteria
import kz.coffee.go.utils.Resource

interface ICafeteriasRepo {
    suspend fun getCafeteriasByCity(city: String): Resource<List<Cafeteria>>
}