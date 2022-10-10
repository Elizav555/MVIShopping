package com.elizav.mvishopping.ui.cart.state

import com.elizav.mvishopping.domain.model.Product

sealed class CartAction {
    data class LoadProducts(val clientId: String) : CartAction()
    data class LoadedAction(val products: List<Product>) : CartAction()

    data class ErrorAction(val errorMsg: String) : CartAction()
}