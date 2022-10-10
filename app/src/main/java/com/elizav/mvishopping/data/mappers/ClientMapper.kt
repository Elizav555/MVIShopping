package com.elizav.mvishopping.data.mappers

import com.elizav.mvishopping.data.mappers.ProductMapper.toData
import com.elizav.mvishopping.data.mappers.ProductMapper.toDomain
import com.elizav.mvishopping.domain.model.Client
import com.elizav.mvishopping.data.model.Client as ClientData

object ClientMapper {
    fun ClientData.toDomain() = Client(
        id = id,
        name = name,
        cart = cart,
        products = products.map { it.toDomain() },
    )

    fun Client.toData() = ClientData(
        id = id,
        name = name,
        cart = cart,
        products = products.map { it.toData() },
    )
}