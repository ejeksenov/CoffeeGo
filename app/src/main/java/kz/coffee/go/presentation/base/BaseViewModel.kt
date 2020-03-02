package kz.coffee.go.presentation.base

import androidx.lifecycle.ViewModel
import kz.coffee.go.data.coroutine.CoroutineContextProvider
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseViewModel: ViewModel(), KoinComponent {
    protected val coroutineContext: CoroutineContextProvider by inject()
}