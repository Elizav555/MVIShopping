package com.elizav.mvishopping.ui.auth.state

import com.elizav.mvishopping.domain.model.Client
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential

sealed class AuthAction {
    object SignInAction : AuthAction()
    object LoadingAction : AuthAction()
    data class BeginSignInResultAction(val result: BeginSignInResult) : AuthAction()
    data class SignInWithCredAction(
        val googleAuthCredential: AuthCredential,
        val account: GoogleSignInAccount
    ) : AuthAction()
    data class SignedInAction(val client: Client) : AuthAction()
    data class ErrorAction(val errorMsg: String) : AuthAction()
}