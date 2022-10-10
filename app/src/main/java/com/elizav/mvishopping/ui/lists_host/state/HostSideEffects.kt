package com.elizav.mvishopping.ui.lists_host.state

import com.elizav.mvishopping.domain.auth.AuthRepository
import com.freeletics.rxredux.SideEffect
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class HostSideEffects @Inject constructor(
    private val authRepository: AuthRepository
) {
    val sideEffects = listOf(
        logoutSideEffect()
    )

    private fun logoutSideEffect(): SideEffect<HostState, HostAction> = { actions, state ->
        actions.ofType<HostAction.LogoutAction>()
            .switchMap {
                authRepository.signOut()
                    .toObservable()
                    ?.map<HostAction> {
                        HostAction.SuccessAction
                    }
                    ?.onErrorReturn { error -> HostAction.ErrorAction(error.message ?: "") }
            }
    }
}

