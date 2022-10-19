package com.elizav.mvishopping.store.hostState

import com.elizav.mvishopping.domain.auth.AuthRepository
import com.elizav.mvishopping.domain.model.AppException.Companion.LOADING_ERROR_MSG
import com.elizav.mvishopping.domain.model.AppException.Companion.LOGOUT_ERROR_MSG
import com.elizav.mvishopping.domain.product.ProductsRepository
import com.freeletics.rxredux.SideEffect
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class HostSideEffects @Inject constructor(
    private val authRepository: AuthRepository,
    private val productsRepository: ProductsRepository
) {
    val sideEffects = listOf(
        logoutSideEffect(),
        logoutObserveSideEffect(),
        observeCartCountSideEffect()
    )

    private fun logoutSideEffect(): SideEffect<HostState, HostAction> = { actions, state ->
        actions.ofType<HostAction.LogoutAction>()
            .switchMap {
                authRepository.signOut()
                    .toObservable()
                    ?.map<HostAction> {
                        HostAction.SuccessLogoutAction
                    }
                    ?.onErrorReturn { error ->
                        HostAction.ErrorAction(
                            error.message ?: LOGOUT_ERROR_MSG
                        )
                    }
            }
    }

    private fun logoutObserveSideEffect(): SideEffect<HostState, HostAction> = { actions, state ->
        authRepository.observeAuthState().filter { !it }
            .map<HostAction> {
                HostAction.SuccessLogoutAction
            }
    }

    private fun observeCartCountSideEffect(): SideEffect<HostState, HostAction> =
        { actions, state ->
            productsRepository.observeProducts(state().clientId)
                .map { list -> list.count { it.isPurchased } }
                .distinctUntilChanged()
                .map<HostAction> { cartCount ->
                    HostAction.CartUpdatedAction(cartCount)
                }.onErrorReturn { error ->
                    HostAction.ErrorAction(
                        error.message ?: LOADING_ERROR_MSG
                    )
                }
        }
}

