package com.elizav.mvishopping.ui.products.state

import com.elizav.mvishopping.domain.client.ClientsRepository
import com.elizav.mvishopping.domain.product.ProductsRepository
import com.freeletics.rxredux.SideEffect
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class ProductsListSideEffects @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val productsRepository: ProductsRepository,
) {
    val sideEffects = listOf(
        loadProductsSideEffect()
    )

    private fun loadProductsSideEffect(): SideEffect<ProductsListState, ProductsListAction> =
        { actions, state ->
            actions.ofType<ProductsListAction.LoadProducts>()
                .switchMap {
                    productsRepository.getAllProducts(it.clientId).toObservable()
                        .map<ProductsListAction> { result ->
                            ProductsListAction.LoadedAction(result)
                        }
                        ?.onErrorReturn { error ->
                            ProductsListAction.ErrorAction(
                                error.message ?: ""
                            )
                        }
                }
        }
}
