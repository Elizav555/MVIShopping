package com.elizav.mvishopping.domain.product

import com.elizav.mvishopping.domain.model.Product
import io.reactivex.Observable
import io.reactivex.Single

interface ProductsRepository {
    fun getAllProducts(clientId: String): Single<List<Product>>
    fun observeProducts(clientId: String): Observable<List<Product>>
    fun getProduct(clientId: String, productId: String): Single<Product>
    fun addProduct(clientId: String, product: Product): Single<Boolean>
    fun deleteProduct(clientId: String, productId: String): Single<Boolean>
}