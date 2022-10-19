package com.elizav.mvishopping.di

import android.app.Application
import android.content.Context
import com.elizav.mvishopping.domain.model.ErrorEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideContext(
        app: Application
    ): Context = app.applicationContext

    @Provides
    fun provideErrorEvent( ) = ErrorEvent()
}