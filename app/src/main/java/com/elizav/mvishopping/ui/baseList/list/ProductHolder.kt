package com.elizav.mvishopping.ui.baseList.list

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.elizav.mvishopping.databinding.ItemProductBinding
import com.elizav.mvishopping.domain.model.Product

class ProductHolder(
    private val binding: ItemProductBinding,
    private val checkedFunc: ((position: Int, isChecked: Boolean, product: Product) -> Unit)?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        with(binding) {
            val isCart = checkedFunc == null
            tvProductName.text = product.name
            checkBoxIsPurchased.isChecked = product.isPurchased
            checkBoxIsPurchased.isVisible = !isCart
            ivFilter.isVisible = !isCart && product.isPurchased
            checkedFunc?.let {
                checkBoxIsPurchased.setOnCheckedChangeListener { _, isChecked ->
                    it(adapterPosition,isChecked,product)
                }
            }
        }
    }
}