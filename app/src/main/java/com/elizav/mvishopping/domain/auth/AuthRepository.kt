package com.elizav.mvishopping.domain.auth

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import io.reactivex.Single

interface AuthRepository {
    val isUserAuthenticated: Boolean

    fun oneTapSignInWithGoogle(): Single<BeginSignInResult>

    fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Single<Boolean>
}