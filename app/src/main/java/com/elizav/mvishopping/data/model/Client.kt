package com.elizav.mvishopping.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Client(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("cart") val cart: List<Int>,
    @SerialName("products") val products: List<Product>,
)