package com.bluboy.android.di


import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.loginsignup.LoginSignupViewModel
import com.bluboy.android.presentation.splash.SplashViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created Koin module for ViewModel class
 */
val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { LoginSignupViewModel(get()) }
}