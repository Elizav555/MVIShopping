package com.elizav.mvishopping.ui.cart.state

import com.elizav.mvishopping.domain.product.ProductsRepository
import com.elizav.mvishopping.ui.baseList.state.ListAction
import com.elizav.mvishopping.ui.baseList.state.ListSideEffects
import com.elizav.mvishopping.ui.baseList.state.ListState
import com.freeletics.rxredux.SideEffect
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class CartSideEffects @Inject constructor(
    private val productsRepository: ProductsRepository,
) : ListSideEffects {
    override val sideEffects = listOf(
        loadProductsSideEffect(),
        sortProductsSideEffect()
    )

    override fun loadProductsSideEffect(): SideEffect<ListState, ListAction> =
        { actions, state ->
            actions.ofType<ListAction.LoadProducts>()
                .switchMap {
                    getProductsForCart(state().clientId)
                }
        }

    private fun getProductsForCart(clientId: String) =
        productsRepository.getAllProducts(clientId).toObservable()
            .map<ListAction> { products ->
                ListAction.LoadedAction(products.filter {
                    it.isPurchased
                }.sortedBy { it.name })
            }.onErrorReturn { error ->
                ListAction.ErrorAction(
                    error.message ?: ""
                )
            }

    override fun sortProductsSideEffect(): SideEffect<ListState, ListAction> =
        { actions, state ->
            actions.ofType<ListAction.SortAction>()
                .switchMap { sortAction ->
                    Observable.create<ListAction> { emitter ->
                        val newProducts = if (sortAction.isDesc) {
                            state().products?.sortedByDescending { it.name }
                        } else {
                            state().products?.sortedBy { it.name }
                        }
                        newProducts?.let { emitter.onNext(ListAction.LoadedAction(newProducts)) }
                            ?: emitter.onError(Exception())
                    }
                }
        }
}

