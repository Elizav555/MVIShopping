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
        sortProductsSideEffect(),
        updateProductSideEffect()
    )

    override fun loadProductsSideEffect(): SideEffect<ListState, ListAction> =
        { actions, state ->
            actions.ofType<ListAction.LoadProducts>()
                .switchMap {
                    getProductsForCart(state().clientId, state())
                }
        }

    private fun getProductsForCart(clientId: String, state: ListState) =
        productsRepository.getAllProducts(clientId).toObservable()
            .map<ListAction> { products ->
                val cartProducts = if (state.isDesc) {
                    products.sortedByDescending { it.name }
                } else {
                    products.sortedBy { it.name }
                }
                ListAction.LoadedAction(cartProducts.filter {
                    it.isPurchased
                })
            }.onErrorReturn { error ->
                ListAction.ErrorAction(
                    error.message ?: ""
                )
            }

    override fun sortProductsSideEffect(): SideEffect<ListState, ListAction> =
        { actions, state ->
            actions.ofType<ListAction.SortAction>()
                .switchMap {
                    Observable.create<ListAction> { emitter ->
                        val newProducts = if (state().isDesc) {
                            state().products?.sortedByDescending { it.name }
                        } else {
                            state().products?.sortedBy { it.name }
                        }
                        newProducts?.let { emitter.onNext(ListAction.LoadedAction(newProducts)) }
                            ?: emitter.onError(Exception())
                    }
                }
        }

    override fun updateProductSideEffect(): SideEffect<ListState, ListAction> = { actions, state ->
        actions.ofType<ListAction.UpdateProductAction>()
            .switchMap { updateAction ->
                val newProducts = state().products?.toMutableList()
                newProducts?.set(updateAction.productPosition, updateAction.updatedProduct)
                productsRepository.addProduct(state().clientId, updateAction.updatedProduct)
                    .toObservable().map<ListAction> {
                    if (!it || newProducts == null) {
                        ListAction.ErrorAction("")
                    } else {
                        ListAction.LoadedAction(newProducts)
                    }
                }.onErrorReturn { ListAction.ErrorAction(it.message ?: "") }
            }
    }
}

