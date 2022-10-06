package com.elizav.mvishopping.ui.navigation

sealed class Screen(val route: String) {
    object AuthScreen : Screen(AUTH_SCREEN)
    object ProfileScreen : Screen(PROFILE_SCREEN)

    companion object {
        const val AUTH_SCREEN = "auth_screen"
        const val PROFILE_SCREEN = "profile_screen"
    }
}