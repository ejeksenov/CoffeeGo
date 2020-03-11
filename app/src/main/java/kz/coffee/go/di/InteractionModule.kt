package kz.coffee.go.di

import kz.coffee.go.domain.cafeteria.ICafeterias
import kz.coffee.go.domain.cafeteria.ICafeteriasImpl
import kz.coffee.go.domain.purchaseHistory.IPurchaseHistory
import kz.coffee.go.domain.purchaseHistory.IPurchaseHistoryImpl
import kz.coffee.go.domain.user.IUsers
import kz.coffee.go.domain.user.IUsersImpl
import org.koin.dsl.module

val interactionModule = module {
    factory<IUsers>{
        IUsersImpl(get())
    }
    factory<ICafeterias>{
        ICafeteriasImpl(get())
    }

    factory<IPurchaseHistory>{
        IPurchaseHistoryImpl(get())
    }

}