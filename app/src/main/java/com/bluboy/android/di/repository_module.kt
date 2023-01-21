package com.bluboy.android.di


import com.bluboy.android.data.contract.HomeRepo
import com.bluboy.android.data.contract.LoginSignupRepo
import com.bluboy.android.data.repository.HomeRepository
import com.bluboy.android.data.repository.LoginSignupApisRepository
import org.koin.dsl.module.module

/**
 * Created Koin module for Repository class
 */

val repositoryModule = module {
    single<LoginSignupRepo> { LoginSignupApisRepository(get()) }
    single<HomeRepo> { HomeRepository(get()) }
}