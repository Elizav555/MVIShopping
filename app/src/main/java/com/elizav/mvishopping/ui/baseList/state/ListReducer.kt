package com.elizav.mvishopping.ui.baseList.state

class ListReducer : (ListState, ListAction) -> ListState {
    override fun invoke(state: ListState, action: ListAction): ListState =
        when (action) {
            is ListAction.ErrorAction -> state.copy(
                isLoading = false, errorMsg = action.errorMsg,
            )
            is ListAction.LoadProducts -> state.copy(
                isLoading = true, errorMsg = null
            )
            is ListAction.LoadedAction -> state.copy(
                isLoading = false, errorMsg = null, products = action.products
            )
            is ListAction.SortAction -> state.copy(
                isLoading = true, errorMsg = null
            )
            is ListAction.UpdateProductAction -> state.copy(
                isLoading = true, errorMsg = null
            )
        }
}
