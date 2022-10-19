package com.elizav.mvishopping.data.client

import com.elizav.mvishopping.domain.model.Client
import com.elizav.mvishopping.data.client.Client as ClientData

class ClientMapper {
    fun map(clientData: ClientData, id: String) = Client(
        id = id,
        name = clientData.name,
    )

    fun map(client: Client) = ClientData(
        name = client.name,
    )
}