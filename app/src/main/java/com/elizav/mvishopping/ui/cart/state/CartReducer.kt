package com.elizav.mvishopping.ui.cart.state

class CartReducer : (CartState, CartAction) -> CartState {
    override fun invoke(state: CartState, action: CartAction): CartState =
        when (action) {
            is CartAction.ErrorAction -> state.copy(
                errorMsg = action.errorMsg, isLoading = false
            )
            is CartAction.LoadProducts -> state.copy(
                isLoading = true, errorMsg = null
            )
            is CartAction.LoadedAction -> state.copy(
                isLoading = false, errorMsg = null, products = action.products
            )
        }
}
