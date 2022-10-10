package com.elizav.mvishopping.domain.model

data class Client(
    val id: String,
    val name: String,
    val cart: List<Int>,
    val products: List<Product>,
)