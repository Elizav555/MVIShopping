package com.elizav.mvishopping.ui.products.state

import com.elizav.mvishopping.domain.product.ProductsRepository
import com.elizav.mvishopping.ui.baseList.state.ListAction
import com.elizav.mvishopping.ui.baseList.state.ListSideEffects
import com.elizav.mvishopping.ui.baseList.state.ListState
import com.freeletics.rxredux.SideEffect
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class ProductsListSideEffects @Inject constructor(
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
                    productsRepository.getAllProducts(state().clientId).toObservable()
                        .map<ListAction> { result ->
                            ListAction.LoadedAction(result.sortedBy { it.name })
                        }
                        ?.onErrorReturn { error ->
                            ListAction.ErrorAction(
                                error.message ?: ""
                            )
                        }
                }
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

    override fun updateProductSideEffect(): SideEffect<ListState, ListAction> = { actions, state ->
        actions.ofType<ListAction.UpdateProductAction>()
            .switchMap { updateAction ->
                val newProducts = state().products?.toMutableList()
                newProducts?.set(updateAction.productPosition,updateAction.updatedProduct)
                productsRepository.addProduct(state().clientId,updateAction.updatedProduct).toObservable().map<ListAction> {
                    if(!it||newProducts==null){
                        ListAction.ErrorAction("")
                    }else{
                        ListAction.LoadedAction(newProducts)
                    }
                }.onErrorReturn {  ListAction.ErrorAction(it.message?:"") }
            }
    }
}
