package com.elizav.mvishopping.di.singleton

import com.elizav.mvishopping.data.client.ClientMapper
import com.elizav.mvishopping.data.product.ProductMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MappersModule {
    @Provides
    fun provideClientMapper(): ClientMapper = ClientMapper()

    @Provides
    fun provideProductsMapper(): ProductMapper = ProductMapper()
}