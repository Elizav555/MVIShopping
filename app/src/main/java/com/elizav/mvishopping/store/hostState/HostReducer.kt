package com.elizav.mvishopping.store.hostState

import com.freeletics.rxredux.Reducer

class HostReducer : Reducer<HostState, HostAction> {

    override fun invoke(state: HostState, action: HostAction): HostState =
        when (action) {
            HostAction.LogoutAction -> state.copy(
                errorMsg = null,
                isLogoutSuccess = false
            )
            is HostAction.ErrorAction -> state.copy(
                errorMsg = action.errorMsg,
                isLogoutSuccess = false
            )
            HostAction.SuccessLogoutAction -> state.copy(
                errorMsg = null,
                isLogoutSuccess = true
            )
            is HostAction.CartUpdatedAction -> state.copy(
                errorMsg = null,
                isLogoutSuccess = false,
                cartCount = action.cartCount
            )
        }
}