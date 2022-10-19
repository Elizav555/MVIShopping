package com.elizav.mvishopping.store.listState

import com.elizav.mvishopping.domain.model.Product

sealed class ListAction {
    data class LoadedAction(val products: List<Product>) : ListAction()
    data class SortAction(val isDesc: Boolean) : ListAction()
    data class UpdateProductAction(val updatedProduct: Product) : ListAction()
    data class AddProductAction(val productName: String) : ListAction()
    data class DeleteProductAction(val productId: String) : ListAction()
}