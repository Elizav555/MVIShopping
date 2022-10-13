package com.elizav.mvishopping.ui.baseList.state

import com.elizav.mvishopping.domain.product.ProductsRepository
import com.elizav.mvishopping.utils.ProductListExtension.sortByName
import com.freeletics.rxredux.SideEffect
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType

abstract class ListSideEffects(private val productsRepository: ProductsRepository) {
    abstract val sideEffects: List<SideEffect<ListState, ListAction>>

    open fun sortProductsSideEffect(): SideEffect<ListState, ListAction> =
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

    open fun updateProductSideEffect(): SideEffect<ListState, ListAction> = { actions, state ->
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

    abstract fun observeProductsSideEffect(): SideEffect<ListState, ListAction>
}