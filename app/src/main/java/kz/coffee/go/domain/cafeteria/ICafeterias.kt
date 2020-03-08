package kz.coffee.go.domain.cafeteria

import kz.coffee.go.utils.Resource

interface ICafeterias {
    suspend fun getCafeteriasByCity(city: String): Resource<List<Cafeteria>>
}