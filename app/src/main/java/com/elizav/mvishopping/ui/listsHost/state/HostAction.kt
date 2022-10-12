package com.elizav.mvishopping.ui.listsHost.state

sealed class HostAction {
    object LogoutAction : HostAction()
    object SuccessLogoutAction : HostAction()
    data class ErrorAction(val errorMsg: String) : HostAction()
}