package com.elizav.mvishopping.domain.model

sealed class AppException(override val message: String) : Exception() {
    class LoadingErrorException(override val message: String = LOADING_ERROR_MSG) :
        AppException(message)

    class AuthErrorException(override val message: String = AUTH_ERROR_MSG) :
        AppException(message)

    class UpdateErrorException(override val message: String = UPDATE_ERROR_MSG) :
        AppException(message)

    class DeleteErrorException(override val message: String = DELETE_ERROR_MSG) :
        AppException(message)

    class LogoutErrorException(override val message: String = LOGOUT_ERROR_MSG) :
        AppException(message)

    companion object {
        const val LOADING_ERROR_MSG = "Loading error"
        const val AUTH_ERROR_MSG = "Auth error"
        const val LOGOUT_ERROR_MSG = "Logout error"
        const val UPDATE_ERROR_MSG = "Error while updating"
        const val DELETE_ERROR_MSG = "Error while deleting"
    }
}
