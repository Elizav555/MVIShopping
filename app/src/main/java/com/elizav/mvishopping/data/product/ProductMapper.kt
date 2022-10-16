package com.elizav.mvishopping.data.product

import com.elizav.mvishopping.domain.model.Product
import com.elizav.mvishopping.data.product.Product as ProductData

object ProductMapper {
    fun ProductData.toDomain(id: String) = Product(
        id = id,
        name = name,
        isPurchased = isPurchased
    )

    fun Product.toData() = ProductData(
        name = name,
        isPurchased = isPurchased
    )
}