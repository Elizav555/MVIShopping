package com.elizav.mvishopping.store.hostState

data class HostState(
    val clientId: String,
    val errorMsg: String? = null,
    val isLogoutSuccess: Boolean = false,
    val cartCount: Int = 0
)
