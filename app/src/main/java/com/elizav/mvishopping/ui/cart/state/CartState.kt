package com.elizav.mvishopping.ui.cart.state

import com.elizav.mvishopping.domain.model.Product

data class CartState(
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val products: List<Product>? = null
)