package kz.coffee.go.di

import kz.coffee.go.data.cafeteria.CafeteriasRepoImpl
import kz.coffee.go.data.cafeteria.ICafeteriasRepo
import kz.coffee.go.data.purchaseHistory.IPurchaseHistoryRepo
import kz.coffee.go.data.purchaseHistory.PurchaseHistoryRepoImpl
import kz.coffee.go.data.user.IUsersRepo
import kz.coffee.go.data.user.UsersRepoImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory<IUsersRepo> {
        UsersRepoImpl()
    }
    factory<ICafeteriasRepo> {
        CafeteriasRepoImpl()
    }
    factory<IPurchaseHistoryRepo> {
        PurchaseHistoryRepoImpl()
    }
}