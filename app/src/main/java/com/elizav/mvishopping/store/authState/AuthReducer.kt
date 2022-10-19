package com.elizav.mvishopping.store.authState

import com.freeletics.rxredux.Reducer

class AuthReducer : Reducer<AuthState, AuthAction> {

    override fun invoke(state: AuthState, action: AuthAction): AuthState =
        when (action) {
            is AuthAction.BeginSignInResultAction -> state.copy(
                beginSignInResult = action.result,
                isLoading = true,
            )
            AuthAction.SignInAction -> state.copy(
                isLoading = true
            )
            is AuthAction.SignedInAction -> state.copy(
                currentClientId = action.clientId,
                isLoading = true,
                beginSignInResult = null
            )
            is AuthAction.SignInWithCredAction -> state.copy(
                isLoading = true,
                beginSignInResult = null
            )
            AuthAction.CheckAuthAction -> state.copy(
                isLoading = true,
                beginSignInResult = null
            )
            AuthAction.LoadedAction -> state.copy(
                isLoading = false,
                beginSignInResult = null
            )
        }
}