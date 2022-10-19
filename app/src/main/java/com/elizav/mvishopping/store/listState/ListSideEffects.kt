package com.elizav.mvishopping.store.listState

import com.elizav.mvishopping.domain.model.AppException
import com.elizav.mvishopping.domain.model.AppException.Companion.UPDATE_ERROR_MSG
import com.elizav.mvishopping.domain.model.ErrorEvent
import com.elizav.mvishopping.domain.product.ProductsRepository
import com.elizav.mvishopping.ui.utils.ProductListExtension.sortByName
import com.freeletics.rxredux.SideEffect
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType

abstract class ListSideEffects(
    private val productsRepository: ProductsRepository, private val errorEvent: ErrorEvent
) {
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
                        } ?: errorEvent.publish(
                            AppException.LoadingErrorException()
                        )
                    }
                }
        }

    open fun addProductSideEffect(): SideEffect<ListState, ListAction> = { actions, state ->
        actions.ofType<ListAction.AddProductAction>()
            .switchMap { addAction ->
                productsRepository.addProduct(state().clientId, addAction.productName)
                    .doOnError { error ->
                        errorEvent.publish(
                            AppException.UpdateErrorException(
                                error.message ?: UPDATE_ERROR_MSG
                            )
                        )
                    }
                Observable.empty()
            }
    }

    open fun updateProductSideEffect(): SideEffect<ListState, ListAction> = { actions, state ->
        actions.ofType<ListAction.UpdateProductAction>()
            .switchMap { updateAction ->
                productsRepository.updateProduct(state().clientId, updateAction.updatedProduct)
                    .toObservable().filter { !it }.doOnError { error ->
                        errorEvent.publish(
                            AppException.UpdateErrorException(
                                error.message ?: UPDATE_ERROR_MSG
                            )
                        )
                    }
                Observable.empty()
            }
    }

    open fun deleteProductSideEffect(): SideEffect<ListState, ListAction> = { actions, state ->
        actions.ofType<ListAction.DeleteProductAction>()
            .switchMap { deleteAction ->
                productsRepository.deleteProduct(
                    state().clientId,
                    deleteAction.productId
                )
                    .toObservable().filter { !it }.doOnError { error ->
                        errorEvent.publish(
                            AppException.UpdateErrorException(
                                error.message ?: UPDATE_ERROR_MSG
                            )
                        )
                    }
                Observable.empty()
            }
    }

    abstract fun observeProductsSideEffect(): SideEffect<ListState, ListAction>
}