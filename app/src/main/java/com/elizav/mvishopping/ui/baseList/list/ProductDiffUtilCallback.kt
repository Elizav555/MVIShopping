package com.elizav.mvishopping.ui.baseList.list

import androidx.recyclerview.widget.DiffUtil
import com.elizav.mvishopping.domain.model.Product

class ProductDiffUtilCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(
        oldItem: Product,
        newItem: Product
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Product,
        newItem: Product
    ): Boolean = oldItem == newItem
}