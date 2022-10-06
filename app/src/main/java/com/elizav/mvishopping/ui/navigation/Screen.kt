package com.elizav.mvishopping.ui.navigation

import com.elizav.mvishopping.utils.Constants.AUTH_SCREEN
import com.elizav.mvishopping.utils.Constants.PROFILE_SCREEN

sealed class Screen(val route: String) {
    object AuthScreen : Screen(AUTH_SCREEN)
    object ProfileScreen : Screen(PROFILE_SCREEN)
}