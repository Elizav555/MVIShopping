package com.elizav.mvishopping.store.authState

import com.freeletics.rxredux.Reducer

class AuthReducer : Reducer<AuthState, AuthAction> {

    override fun invoke(state: AuthState, action: AuthAction): AuthState =
        when (action) {
            is AuthAction.BeginSignInResultAction -> state.copy(
                beginSignInResult = action.result,
                isLoading = true,
                errorMsg = null
            )
            is AuthAction.ErrorAction -> state.copy(
                errorMsg = action.errorMsg,
                isLoading = false,
                beginSignInResult = null
            )
            AuthAction.SignInAction -> state.copy(
                isLoading = true
            )
            is AuthAction.SignedInAction -> state.copy(
                currentClientId = action.clientId,
                isLoading = true,
                errorMsg = null,
                beginSignInResult = null
            )
            is AuthAction.SignInWithCredAction -> state.copy(
                isLoading = true,
                errorMsg = null,
                beginSignInResult = null
            )
            AuthAction.CheckAuthAction -> state.copy(
                isLoading = true,
                errorMsg = null,
                beginSignInResult = null
            )
            AuthAction.LoadedAction -> state.copy(
                isLoading = false,
                errorMsg = null,
                beginSignInResult = null
            )
        }
}