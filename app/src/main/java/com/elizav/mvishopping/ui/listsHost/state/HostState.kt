package com.elizav.mvishopping.ui.listsHost.state

data class HostState(
    val clientId: String,
    val errorMsg: String? = null,
    val isLogoutSuccess: Boolean = false,
    val cartCount: Int = 0
)
