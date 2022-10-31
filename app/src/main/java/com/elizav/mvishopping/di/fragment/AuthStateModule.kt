package com.elizav.mvishopping.di.fragment

import com.elizav.mvishopping.domain.auth.AuthRepository
import com.elizav.mvishopping.rx.CacheRelay
import com.elizav.mvishopping.store.authState.*
import com.freeletics.rxredux.reduxStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import io.reactivex.Observable
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
class AuthStateModule {

    @Provides
    @Singleton
    fun provideActions(): CacheRelay<AuthAction> {
        return CacheRelay.create()
    }

    @Provides
    @Singleton
    fun provideEffects(): CacheRelay<AuthEffect> {
        return CacheRelay.create()
    }

    @Provides
    fun provideAuthStateObservable(
        actions: CacheRelay<AuthAction>,
        authSideEffects: AuthSideEffects
    ): Observable<AuthState> {
        return actions.reduxStore(
            AuthState(),
            authSideEffects.sideEffects,
            AuthReducer()
        )
            .distinctUntilChanged()
    }

    @Provides
    fun provideAuthSideEffects(
        authRepository: AuthRepository,
        effects: CacheRelay<AuthEffect>
    ): AuthSideEffects = AuthSideEffects(authRepository, effects)
}