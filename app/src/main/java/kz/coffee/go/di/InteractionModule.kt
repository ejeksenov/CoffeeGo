package kz.coffee.go.di

import kz.coffee.go.domain.IUsers
import kz.coffee.go.domain.IUsersImpl
import org.koin.dsl.module

val interactionModule = module {
    factory<IUsers>{
        IUsersImpl(get())
    }
}