package com.elizav.mvishopping.di

import com.elizav.mvishopping.data.auth.AuthRepositoryImpl
import com.elizav.mvishopping.data.auth.ProfileRepositoryImpl
import com.elizav.mvishopping.domain.auth.AuthRepository
import com.elizav.mvishopping.domain.auth.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindsModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        AuthRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        ProfileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository
}