package com.elizav.mvishopping.store.authState

import com.freeletics.rxredux.Reducer

class AuthReducer : Reducer<AuthState, AuthAction> {

    override fun invoke(state: AuthState, action: AuthAction): AuthState =
        when (action) {
            is AuthAction.BeginSignInResultAction -> state.copy(
                isLoading = true
            )
            is AuthAction.ErrorAction -> state.copy(
                isLoading = false,
            )
            AuthAction.SignInAction -> state.copy(
                isLoading = true
            )
            is AuthAction.SignedInAction -> state.copy(
                isLoading = true
            )
            is AuthAction.SignInWithCredAction -> state.copy(
                isLoading = true
            )
            AuthAction.CheckAuthAction -> state.copy(
                isLoading = true
            )
            AuthAction.LoadedAction -> state.copy(
                isLoading = false
            )
        }
}