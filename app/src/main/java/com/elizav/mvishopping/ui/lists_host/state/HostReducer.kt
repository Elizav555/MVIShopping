package com.elizav.mvishopping.ui.lists_host.state

import com.freeletics.rxredux.Reducer

class HostReducer : Reducer<HostState, HostAction> {

    override fun invoke(state: HostState, action: HostAction): HostState =
        when (action) {
            HostAction.LogoutAction -> state.copy(
                errorMsg = null,
                isSuccess = false
            )
            is HostAction.ErrorAction -> state.copy(
                errorMsg = action.errorMsg,
                isSuccess = false
            )
            HostAction.SuccessAction -> state.copy(
                errorMsg = null,
                isSuccess = true
            )
        }
}