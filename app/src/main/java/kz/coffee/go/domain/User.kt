package kz.coffee.go.domain

const val userCollectionName = "Users"

data class User(var fullName: String? = "",
                var cashback: Double? = 0.0,
                var city: String? = "",
                var imageUrl: String? = "")