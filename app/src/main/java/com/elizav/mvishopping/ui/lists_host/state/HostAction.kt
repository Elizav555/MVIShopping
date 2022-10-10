package com.elizav.mvishopping.ui.lists_host.state

sealed class HostAction {
    object LogoutAction : HostAction()
    object SuccessLogoutAction : HostAction()
    data class ErrorAction(val errorMsg: String) : HostAction()
}