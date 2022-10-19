package com.elizav.mvishopping.store.hostState

import com.freeletics.rxredux.Reducer

class HostReducer : Reducer<HostState, HostAction> {

    override fun invoke(state: HostState, action: HostAction): HostState =
        when (action) {
            HostAction.LogoutAction -> state.copy(
                isLogoutSuccess = false
            )
            HostAction.SuccessLogoutAction -> state.copy(
                isLogoutSuccess = true
            )
            is HostAction.CartUpdatedAction -> state.copy(
                isLogoutSuccess = false,
                cartCount = action.cartCount
            )
        }
}