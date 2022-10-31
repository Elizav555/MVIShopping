package com.elizav.mvishopping.store.authState

import android.content.Intent

sealed class AuthAction {
    object CheckAuthAction : AuthAction()
    object SignInAction : AuthAction()
    object LoadedAction : AuthAction()
    object BeginSignInResultAction : AuthAction()
    data class SignInWithCredAction(
        val intentData: Intent
    ) : AuthAction()

    object SignedInAction : AuthAction()
    object ErrorAction : AuthAction()
}