package kz.coffee.go.presentation.main

import com.google.firebase.auth.FirebaseAuth
import kz.coffee.go.presentation.base.BaseViewModel

class MainViewModel : BaseViewModel() {

    fun checkUserSignedIn(): Boolean {
        val mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser == null)
            return false
        return true
    }

}