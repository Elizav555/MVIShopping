package com.elizav.mvishopping.domain.auth

import android.content.Intent
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import io.reactivex.Single

interface AuthRepository {
    val currentClient: FirebaseUser?

    val isUserAuthenticated: Boolean

    fun observeAuthState(): Observable<Boolean>

    fun oneTapSignInWithGoogle(): Single<BeginSignInResult>

    fun firebaseSignInWithGoogle(intentData: Intent): Single<FirebaseUser>

    fun signOut(): Single<Boolean>
}