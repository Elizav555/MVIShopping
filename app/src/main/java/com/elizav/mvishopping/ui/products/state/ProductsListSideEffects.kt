package com.elizav.mvishopping.ui.products.state

import com.elizav.mvishopping.domain.product.ProductsRepository
import com.elizav.mvishopping.ui.baseList.state.ListAction
import com.elizav.mvishopping.ui.baseList.state.ListSideEffects
import com.elizav.mvishopping.ui.baseList.state.ListState
import com.elizav.mvishopping.utils.ProductListExtension.sortByName
import com.freeletics.rxredux.SideEffect
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class ProductsListSideEffects @Inject constructor(
    private val productsRepository: ProductsRepository,
) : ListSideEffects {
    override val sideEffects = listOf(
        sortProductsSideEffect(),
        updateProductSideEffect(),
        observeProductsSideEffect()
    )

    override fun sortProductsSideEffect(): SideEffect<ListState, ListAction> =
        { actions, state ->
            actions.ofType<ListAction.SortAction>()
                .switchMap {
                    Observable.create<ListAction> { emitter ->
                        state().products?.let {
                            emitter.onNext(
                                ListAction.LoadedAction(
                                    it.sortByName(
                                        state().isDesc
                                    )
                                )
                            )
                        } ?: emitter.onError(Exception())
                    }
                }
        }

    override fun updateProductSideEffect(): SideEffect<ListState, ListAction> = { actions, state ->
        actions.ofType<ListAction.UpdateProductAction>()
            .switchMap { updateAction ->
                val newProducts = state().products?.toMutableList()
                newProducts?.set(updateAction.productPosition, updateAction.updatedProduct)
                productsRepository.addProduct(state().clientId, updateAction.updatedProduct)
                    .toObservable().filter { !it || newProducts == null }.map<ListAction> {
                        ListAction.ErrorAction("")
                    }.onErrorReturn { ListAction.ErrorAction(it.message ?: "") }
            }
    }

    override fun observeProductsSideEffect(): SideEffect<ListState, ListAction> =
        { actions, state ->
            productsRepository.observeProducts(state().clientId).map<ListAction> {
                ListAction.LoadedAction(it.sortByName(state().isDesc))
            }.onErrorReturn { ListAction.ErrorAction(it.message ?: "") }
        }
}
