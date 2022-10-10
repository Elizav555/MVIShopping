package com.elizav.mvishopping.data.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Client(
    @SerialName("name") val name: String="",
)