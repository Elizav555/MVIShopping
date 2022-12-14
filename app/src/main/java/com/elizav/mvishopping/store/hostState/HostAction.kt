package com.elizav.mvishopping.store.hostState

sealed class HostAction {
    object LogoutAction : HostAction()
    object SuccessLogoutAction : HostAction()
    data class ErrorAction(val errorMsg: String) : HostAction()
    data class CartUpdatedAction(val cartCount: Int) : HostAction()
}