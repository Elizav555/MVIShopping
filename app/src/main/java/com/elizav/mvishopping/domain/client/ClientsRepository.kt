package com.elizav.mvishopping.domain.client

import com.elizav.mvishopping.domain.model.Client
import com.elizav.mvishopping.domain.model.Product
import io.reactivex.Observable
import io.reactivex.Single

interface ClientsRepository {
    fun getAllClients(): Single<List<Client>>
    fun observeClients(): Observable<List<Client>>
    fun getClient(clientId: String): Single<Client>
    fun addClient(client: Client): Single<Boolean>
    fun deleteClient(clientId: String): Single<Boolean>
    fun updateProducts(clientId: String, products: List<Product>): Single<Boolean>
    fun updateCart(clientId: String, cart: List<Int>): Single<Boolean>
    fun updateName(clientId: String, name: String): Single<Boolean>
}