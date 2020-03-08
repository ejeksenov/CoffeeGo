package kz.coffee.go.domain.cafeteria

import com.google.firebase.firestore.IgnoreExtraProperties

const val cafeteriasCollectionName = "Cafeterias"

@IgnoreExtraProperties
data class Cafeteria(
    var cafeteriaId: String? = "",
    var address: String? = "",
    var city: String? = "",
    var imageUrl: String? = "",
    var logoUrl: String? = "",
    var name: String? = "",
    var phoneNumber: MutableList<String> = ArrayList(),
    var products: MutableList<Product> = ArrayList(),
    var qrCode: String? = "",
    var workingTime: String? = ""
)


data class Product(
    var name: String? = "",
    var price: Double? = 0.0,
    var type: String? = ""
)