package com.elizav.mvishopping.ui.products.state

class ProductsListReducer : (ProductsListState, ProductsListAction) -> ProductsListState {
    override fun invoke(state: ProductsListState, action: ProductsListAction): ProductsListState =
        when (action) {
            is ProductsListAction.ErrorAction -> state.copy(
                errorMsg = action.errorMsg, isLoading = false
            )
            is ProductsListAction.LoadProducts -> state.copy(
                isLoading = true, errorMsg = null
            )
            is ProductsListAction.LoadedAction -> state.copy(
                isLoading = false, errorMsg = null, products = action.products
            )
        }
}
