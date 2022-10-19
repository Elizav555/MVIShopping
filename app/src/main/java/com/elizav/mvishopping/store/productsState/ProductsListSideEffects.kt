package com.elizav.mvishopping.store.productsState

import com.elizav.mvishopping.domain.model.AppException
import com.elizav.mvishopping.domain.model.ErrorEvent
import com.elizav.mvishopping.domain.product.ProductsRepository
import com.elizav.mvishopping.store.listState.ListAction
import com.elizav.mvishopping.store.listState.ListSideEffects
import com.elizav.mvishopping.store.listState.ListState
import com.elizav.mvishopping.ui.utils.ProductListExtension.sortByName
import com.freeletics.rxredux.SideEffect
import javax.inject.Inject

class ProductsListSideEffects @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val errorEvent: ErrorEvent
) : ListSideEffects(productsRepository, errorEvent) {
    override val sideEffects: List<SideEffect<ListState, ListAction>> = listOf(
        sortProductsSideEffect(),
        addProductSideEffect(),
        updateProductSideEffect(),
        deleteProductSideEffect(),
        observeProductsSideEffect()
    )

    override fun observeProductsSideEffect(): SideEffect<ListState, ListAction> =
        { actions, state ->
            productsRepository.observeProducts(state().clientId).map<ListAction> {
                ListAction.LoadedAction(it.sortByName(state().isDesc))
            }.doOnError { error ->
                errorEvent.publish(
                    AppException.LoadingErrorException(
                        error.message ?: AppException.LOADING_ERROR_MSG
                    )
                )
            }
        }
}
