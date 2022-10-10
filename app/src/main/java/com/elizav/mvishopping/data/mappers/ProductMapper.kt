package com.elizav.mvishopping.data.mappers

import com.elizav.mvishopping.domain.model.Product
import com.elizav.mvishopping.data.model.Product as ProductData

object ProductMapper {
    fun ProductData.toDomain() = Product(
        id = id,
        name = name,
        isPurchased = isPurchased
    )

    fun Product.toData() = ProductData(
        id = id,
        name = name,
        isPurchased = isPurchased
    )
}