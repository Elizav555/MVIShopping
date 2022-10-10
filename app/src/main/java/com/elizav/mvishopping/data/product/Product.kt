package com.elizav.mvishopping.data.product

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("name") val name: String = "",
    @field:JvmField
    @SerialName("isPurchased") val isPurchased: Boolean = false,
)
