package com.elizav.mvishopping.ui.products.state

import com.elizav.mvishopping.domain.model.Product

sealed class ProductsListAction {
    data class LoadProducts(val clientId: String) : ProductsListAction()
    data class LoadedAction(val products: List<Product>) : ProductsListAction()

    data class ErrorAction(val errorMsg: String) : ProductsListAction()
}