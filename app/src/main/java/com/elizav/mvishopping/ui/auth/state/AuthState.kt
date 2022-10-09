package com.elizav.mvishopping.ui.auth.state

import com.google.android.gms.auth.api.identity.BeginSignInResult

data class AuthState(
    val currentClientId: String? = null,
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val beginSignInResult: BeginSignInResult? = null
)
