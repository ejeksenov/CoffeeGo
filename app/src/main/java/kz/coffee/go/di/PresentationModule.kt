package kz.coffee.go.di

import kz.coffee.go.presentation.login.LoginViewModel
import kz.coffee.go.presentation.main.MainViewModel
import kz.coffee.go.presentation.signUpEmail.SignUpEmailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {MainViewModel()}
    viewModel {LoginViewModel(get())}
    viewModel {SignUpEmailViewModel(get())}
}