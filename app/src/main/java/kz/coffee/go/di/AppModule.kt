package kz.coffee.go.di

import kz.coffee.go.data.coroutine.CoroutineContextProvider
import org.koin.dsl.module

val appModule = module {
    single { CoroutineContextProvider() }
}