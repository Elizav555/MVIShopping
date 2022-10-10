package com.elizav.mvishopping.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("isPurchased") val isPurchased: Boolean,
)
