package com.elizav.mvishopping.ui.auth.state

import com.freeletics.rxredux.Reducer

class AuthReducer : Reducer<AuthState, AuthAction> {

    override fun invoke(state: AuthState, action: AuthAction): AuthState =
        when (action) {
            is AuthAction.BeginSignInResultAction -> state.copy(
                beginSignInResult = action.result,
                isLoading = false,
                errorMsg = null
            )
            is AuthAction.ErrorAction -> state.copy(
                errorMsg = action.errorMsg,
                isLoading = false,
                beginSignInResult = null
            )
            AuthAction.LoadingAction -> state.copy(
                isLoading = true,
                errorMsg = null,
                beginSignInResult = null
            )
            AuthAction.SignInAction -> state
            is AuthAction.SignedInAction -> state.copy(
                currentClientId = action.clientId,
                isLoading = false,
                errorMsg = null,
                beginSignInResult = null
            )
            is AuthAction.SignInWithCredAction -> state.copy(
                isLoading = true,
                errorMsg = null,
                beginSignInResult = null
            )
        }
}