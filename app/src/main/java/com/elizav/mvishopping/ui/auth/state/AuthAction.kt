package com.elizav.mvishopping.ui.auth.state

import android.content.Intent
import com.google.android.gms.auth.api.identity.BeginSignInResult

sealed class AuthAction {
    object CheckAuthAction : AuthAction()
    object SignInAction : AuthAction()
    object LoadedAction : AuthAction()
    data class BeginSignInResultAction(val result: BeginSignInResult) : AuthAction()
    data class SignInWithCredAction(
        val intentData: Intent
    ) : AuthAction()

    data class SignedInAction(val clientId: String) : AuthAction()
    data class ErrorAction(val errorMsg: String) : AuthAction()
}