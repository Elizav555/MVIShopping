package com.elizav.mvishopping.store.listState

class ListReducer : (ListState, ListAction) -> ListState {
    override fun invoke(state: ListState, action: ListAction): ListState =
        when (action) {
            is ListAction.ErrorAction -> state.copy(
                isLoading = false, errorMsg = action.errorMsg,
            )
            is ListAction.LoadedAction -> state.copy(
                isLoading = false, errorMsg = null, products = action.products
            )
            is ListAction.SortAction -> state.copy(
                isLoading = true, errorMsg = null, isDesc = action.isDesc
            )
            is ListAction.UpdateProductAction -> state.copy(
                isLoading = false, errorMsg = null
            )
            is ListAction.AddProductAction -> state.copy(
                isLoading = false, errorMsg = null
            )
            is ListAction.DeleteProductAction -> state.copy(
                isLoading = false, errorMsg = null
            )
        }
}
