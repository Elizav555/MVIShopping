package com.elizav.mvishopping.store.authState

import com.google.android.gms.auth.api.identity.BeginSignInResult

sealed class AuthEffect {
    class ShowError(val text: String) : AuthEffect()
    class NavigateToList(val clientId: String) : AuthEffect()
    class LaunchIntent(val signInResult: BeginSignInResult) : AuthEffect()
}