package kz.coffee.go.data.cafeteria

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kz.coffee.go.domain.cafeteria.Cafeteria
import kz.coffee.go.domain.cafeteria.cafeteriasCollectionName
import kz.coffee.go.utils.Resource

class CafeteriasRepoImpl: ICafeteriasRepo {

    private val mFirestore = FirebaseFirestore.getInstance()

    override suspend fun getCafeteriasByCity(city: String): Resource<List<Cafeteria>> {
        val cafeteriaList: MutableList<Cafeteria> = ArrayList()
        val listOfDocuments = mFirestore.collection(cafeteriasCollectionName).whereEqualTo("city", city).get().await()
        for (document in listOfDocuments) {
            val cafeteria = document.toObject(Cafeteria::class.java)
            cafeteria.cafeteriaId = document.id
            cafeteriaList.add(cafeteria)
        }
        return Resource.Success(cafeteriaList)
    }


}