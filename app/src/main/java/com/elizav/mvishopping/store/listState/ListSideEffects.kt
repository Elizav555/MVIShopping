package com.elizav.mvishopping.store.listState

import com.elizav.mvishopping.domain.model.AppException.Companion.DELETE_ERROR_MSG
import com.elizav.mvishopping.domain.model.AppException.Companion.UPDATE_ERROR_MSG
import com.elizav.mvishopping.domain.product.ProductsRepository
import com.elizav.mvishopping.ui.utils.ProductListExtension.sortByName
import com.freeletics.rxredux.SideEffect
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType

abstract class ListSideEffects(private val productsRepository: ProductsRepository) {
    abstract val sideEffects: List<SideEffect<ListState, ListAction>>

    open fun sortProductsSideEffect(): SideEffect<ListState, ListAction> =
        { actions, state ->
            actions.ofType<ListAction.SortAction>()
                .switchMap {
                    Observable.create { emitter ->
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

    open fun addProductSideEffect(): SideEffect<ListState, ListAction> = { actions, state ->
        actions.ofType<ListAction.AddProductAction>()
            .switchMap { addAction ->
                productsRepository.addProduct(state().clientId, addAction.productName)
                    .toObservable()
                    .filter { it.isBlank() }
                    .map { return@map ListAction.ErrorAction(UPDATE_ERROR_MSG) }
                    .onErrorReturn { ListAction.ErrorAction(it.message ?: UPDATE_ERROR_MSG) }
            }
    }

    open fun updateProductSideEffect(): SideEffect<ListState, ListAction> = { actions, state ->
        actions.ofType<ListAction.UpdateProductAction>()
            .switchMap { updateAction ->
                productsRepository.updateProduct(state().clientId, updateAction.updatedProduct)
                    .toObservable().filter { !it }.map<ListAction> {
                        ListAction.ErrorAction(UPDATE_ERROR_MSG)
                    }.onErrorReturn { ListAction.ErrorAction(it.message ?: UPDATE_ERROR_MSG) }
            }
    }

    open fun deleteProductSideEffect(): SideEffect<ListState, ListAction> = { actions, state ->
        actions.ofType<ListAction.DeleteProductAction>()
            .switchMap { deleteAction ->
                productsRepository.deleteProduct(
                    state().clientId,
                    deleteAction.productId
                )
                    .toObservable().filter { !it }.map<ListAction> {
                        ListAction.ErrorAction(DELETE_ERROR_MSG)
                    }.onErrorReturn { ListAction.ErrorAction(it.message ?: DELETE_ERROR_MSG) }
            }
    }

    abstract fun observeProductsSideEffect(): SideEffect<ListState, ListAction>
}