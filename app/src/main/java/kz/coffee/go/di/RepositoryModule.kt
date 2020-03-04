package kz.coffee.go.di

import kz.coffee.go.data.user.IUsersRepo
import kz.coffee.go.data.user.UsersRepoImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory<IUsersRepo> {
        UsersRepoImpl()
    }
}