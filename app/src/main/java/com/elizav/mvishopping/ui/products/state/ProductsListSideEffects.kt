package com.elizav.mvishopping.ui.products.state

import com.elizav.mvishopping.domain.product.ProductsRepository
import com.elizav.mvishopping.ui.baseList.state.ListAction
import com.elizav.mvishopping.ui.baseList.state.ListSideEffects
import com.elizav.mvishopping.ui.baseList.state.ListState
import com.elizav.mvishopping.utils.ProductListExtension.sortByName
import com.freeletics.rxredux.SideEffect
import javax.inject.Inject

class ProductsListSideEffects @Inject constructor(
    private val productsRepository: ProductsRepository,
) : ListSideEffects(productsRepository) {
    override val sideEffects: List<SideEffect<ListState, ListAction>> = listOf(
        sortProductsSideEffect(),
        updateProductSideEffect(),
        deleteProductSideEffect(),
        observeProductsSideEffect()
    )

    override fun observeProductsSideEffect(): SideEffect<ListState, ListAction> =
        { actions, state ->
            productsRepository.observeProducts(state().clientId).map<ListAction> {
                ListAction.LoadedAction(it.sortByName(state().isDesc))
            }.onErrorReturn { ListAction.ErrorAction(it.message ?: "") }
        }
}
