package com.elizav.mvishopping.store.hostState

data class HostState(
    val clientId: String,
    val isLogoutSuccess: Boolean = false,
    val cartCount: Int = 0
)
