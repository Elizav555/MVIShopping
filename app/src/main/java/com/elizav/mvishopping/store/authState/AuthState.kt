package com.elizav.mvishopping.store.authState

import com.google.android.gms.auth.api.identity.BeginSignInResult

data class AuthState(
    val currentClientId: String? = null,
    val isLoading: Boolean = false,
    val beginSignInResult: BeginSignInResult? = null
)
