package com.elizav.mvishopping.ui.listsHost.state

import com.elizav.mvishopping.domain.auth.AuthRepository
import com.elizav.mvishopping.domain.model.AppException.Companion.LOGOUT_ERROR_MSG
import com.freeletics.rxredux.SideEffect
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class HostSideEffects @Inject constructor(
    private val authRepository: AuthRepository
) {
    val sideEffects = listOf(
        logoutSideEffect(),
        logoutObserveSideEffect()
    )

    private fun logoutSideEffect(): SideEffect<HostState, HostAction> = { actions, state ->
        actions.ofType<HostAction.LogoutAction>()
            .switchMap {
                authRepository.signOut()
                    .toObservable()
                    ?.map<HostAction> {
                        HostAction.SuccessLogoutAction
                    }
                    ?.onErrorReturn { error -> HostAction.ErrorAction(error.message ?: LOGOUT_ERROR_MSG) }
            }
    }

    private fun logoutObserveSideEffect(): SideEffect<HostState, HostAction> = { actions, state ->
        authRepository.observeAuthState().filter { !it }
            .map<HostAction> {
            HostAction.SuccessLogoutAction
        }
    }
}

