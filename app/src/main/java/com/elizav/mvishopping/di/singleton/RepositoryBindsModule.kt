package com.elizav.mvishopping.di.singleton

import com.elizav.mvishopping.data.auth.AuthRepositoryImpl
import com.elizav.mvishopping.data.client.ClientsRepositoryImpl
import com.elizav.mvishopping.data.product.ProductsRepositoryImpl
import com.elizav.mvishopping.domain.auth.AuthRepository
import com.elizav.mvishopping.domain.client.ClientsRepository
import com.elizav.mvishopping.domain.product.ProductsRepository
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
    abstract fun bindClientsRepository(
        ClientsRepositoryImpl: ClientsRepositoryImpl
    ): ClientsRepository

    @Binds
    @Singleton
    abstract fun bindProductsRepository(
        ProductsRepositoryImpl:ProductsRepositoryImpl
    ): ProductsRepository
}