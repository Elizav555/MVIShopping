package com.elizav.mvishopping.ui.cart.state

import com.elizav.mvishopping.domain.product.ProductsRepository
import com.freeletics.rxredux.SideEffect
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class CartSideEffects @Inject constructor(
    private val productsRepository: ProductsRepository,
) {
    val sideEffects = listOf(
        loadProductsSideEffect()
    )

    private fun loadProductsSideEffect(): SideEffect<CartState, CartAction> =
        { actions, state ->
            actions.ofType<CartAction.LoadProducts>()
                .switchMap { loadProducts ->
                    getProductsForCart(loadProducts.clientId)
                }
        }

    private fun getProductsForCart(clientId: String) =
        productsRepository.getAllProducts(clientId).toObservable()
            .map<CartAction> { products ->
                CartAction.LoadedAction(products.filter {
                    it.isPurchased
                }.sortedBy { it.name })
            }.onErrorReturn { error ->
                CartAction.ErrorAction(
                    error.message ?: ""
                )
            }
}

