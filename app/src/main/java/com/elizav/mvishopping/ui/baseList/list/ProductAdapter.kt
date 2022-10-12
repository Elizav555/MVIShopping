package com.elizav.mvishopping.ui.baseList.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elizav.mvishopping.databinding.ItemProductBinding
import com.elizav.mvishopping.domain.model.Product

class ProductAdapter(
    private val checkedFunc: ((position: Int, isChecked: Boolean, product: Product) -> Unit)?,
) : ListAdapter<Product, ProductHolder>(ProductDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductHolder = ProductHolder(
        binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        checkedFunc = checkedFunc
    )

    override fun onBindViewHolder(
        holder: ProductHolder,
        position: Int
    ) {
        val product = getItem(position)
        holder.bind(product)
    }

    override fun submitList(list: List<Product>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}