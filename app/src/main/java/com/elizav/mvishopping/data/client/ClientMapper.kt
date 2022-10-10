package com.elizav.mvishopping.data.client

import com.elizav.mvishopping.domain.model.Client
import com.elizav.mvishopping.domain.model.Product
import com.elizav.mvishopping.data.client.Client as ClientData

object ClientMapper {
    fun ClientData.toDomain(id:String) = Client(
        id = id,
        name = name,
    )

    fun Client.toData() = ClientData(
        name = name,
    )
}