package com.elizav.mvishopping.ui.baseList.state

import com.elizav.mvishopping.domain.model.Product

data class ListState(
    val clientId: String,
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val products: List<Product>? = null
)