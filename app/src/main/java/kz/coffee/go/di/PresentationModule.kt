package kz.coffee.go.di

import kz.coffee.go.presentation.changeEmail.ChangeEmailViewModel
import kz.coffee.go.presentation.changePassword.ChangePasswordViewModel
import kz.coffee.go.presentation.login.LoginViewModel
import kz.coffee.go.presentation.main.MainViewModel
import kz.coffee.go.presentation.profile.ProfileViewModel
import kz.coffee.go.presentation.signUpEmail.SignUpEmailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {MainViewModel()}
    viewModel {LoginViewModel(get())}
    viewModel {SignUpEmailViewModel(get())}
    viewModel { ProfileViewModel(get())}
    viewModel { ChangeEmailViewModel(get())}
    viewModel { ChangePasswordViewModel(get())}
}