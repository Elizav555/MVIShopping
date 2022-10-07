package com.elizav.mvishopping.ui.auth.state

import com.freeletics.rxredux.Reducer

class AuthReducer : Reducer<AuthState, AuthAction> {

    override fun invoke(state: AuthState, action: AuthAction): AuthState =
        when (action) {
            is AuthAction.BeginSignInResultAction -> state.copy(
                beginSignInResult = action.result,
                isLoading = false,
                errorMsg = null
            ) //TODO launch intent
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
            AuthAction.SignInAction -> state //side effect should handle this action
            is AuthAction.SignedInAction -> state.copy(
                currentClient = action.client,
                isLoading = false,
                errorMsg = null,
                beginSignInResult = null
            )
            is AuthAction.SignInWithCredAction -> state
        }
}