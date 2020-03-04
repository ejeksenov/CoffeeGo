package kz.coffee.go

import android.app.Application
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import kz.coffee.go.di.appModule
import kz.coffee.go.di.interactionModule
import kz.coffee.go.di.presentationModule
import kz.coffee.go.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: MultiDexApplication() {

    companion object {
        lateinit var instance: Application
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        startKoin {
            androidContext(this@MyApplication)
            if (BuildConfig.DEBUG) androidLogger(Level.DEBUG)
            modules(appModules + domainModules + dataModules)
        }
    }
}

val appModules = listOf(presentationModule, appModule)
val domainModules = listOf(interactionModule)
val dataModules = listOf(repositoryModule)