package com.elizav.mvishopping.store.listState

import com.elizav.mvishopping.domain.model.Product

data class ListState(
    val clientId: String,
    val isLoading: Boolean = false,
    val products: List<Product>? = null,
    val isDesc: Boolean = false
)