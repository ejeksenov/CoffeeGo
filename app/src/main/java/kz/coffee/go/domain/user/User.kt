package kz.coffee.go.domain.user

import com.google.firebase.firestore.IgnoreExtraProperties

const val userCollectionName = "Users"

@IgnoreExtraProperties
data class User(var fullName: String? = "",
                var cashback: Double? = 0.0,
                var city: String? = "",
                var imageUrl: String? = "")