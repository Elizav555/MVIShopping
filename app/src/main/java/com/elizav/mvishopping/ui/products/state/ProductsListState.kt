package com.elizav.mvishopping.ui.products.state

import com.elizav.mvishopping.domain.model.Product

data class ProductsListState(
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val products: List<Product>? = null
)