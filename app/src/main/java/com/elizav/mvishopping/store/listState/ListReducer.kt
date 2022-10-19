package com.elizav.mvishopping.store.listState

class ListReducer : (ListState, ListAction) -> ListState {
    override fun invoke(state: ListState, action: ListAction): ListState =
        when (action) {
            is ListAction.LoadedAction -> state.copy(
                isLoading = false,
                products = action.products
            )
            is ListAction.SortAction -> state.copy(
                isLoading = true,
                isDesc = action.isDesc
            )
            is ListAction.UpdateProductAction -> state.copy(
                isLoading = false,
            )
            is ListAction.AddProductAction -> state.copy(
                isLoading = false,
            )
            is ListAction.DeleteProductAction -> state.copy(
                isLoading = false,
            )
        }
}
