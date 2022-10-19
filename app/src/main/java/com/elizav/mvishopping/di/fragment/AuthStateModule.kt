package com.elizav.mvishopping.di.fragment

import com.elizav.mvishopping.domain.auth.AuthRepository
import com.elizav.mvishopping.domain.model.ErrorEvent
import com.elizav.mvishopping.store.authState.AuthReducer
import com.elizav.mvishopping.store.authState.AuthSideEffects
import com.elizav.mvishopping.store.authState.AuthState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
class AuthStateModule {
    @Provides
    fun provideAuthState(): AuthState = AuthState()

    @Provides
    fun provideAuthReducer(): AuthReducer = AuthReducer()

    @Provides
    fun provideAuthSideEffects(authRepository: AuthRepository, errorEvent: ErrorEvent): AuthSideEffects =
        AuthSideEffects(authRepository,errorEvent)
}