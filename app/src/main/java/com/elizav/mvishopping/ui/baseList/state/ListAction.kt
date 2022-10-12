package com.elizav.mvishopping.ui.baseList.state

import com.elizav.mvishopping.domain.model.Product

sealed class ListAction {
    object LoadProducts : ListAction()
    data class LoadedAction(val products: List<Product>) : ListAction()
    data class ErrorAction(val errorMsg: String) : ListAction()
    data class SortAction(val isDesc: Boolean) : ListAction()
    data class UpdateProductAction(val productPosition: Int, val updatedProduct: Product) : ListAction()
}