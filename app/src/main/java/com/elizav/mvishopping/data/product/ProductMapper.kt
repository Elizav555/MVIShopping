package com.elizav.mvishopping.data.product

import com.elizav.mvishopping.domain.model.Product
import com.elizav.mvishopping.data.product.Product as ProductData

class ProductMapper {
    fun map(productData: ProductData, id: String) = Product(
        id = id,
        name = productData.name,
        isPurchased = productData.isPurchased
    )

    fun map(product: Product) = ProductData(
        name = product.name,
        isPurchased = product.isPurchased
    )
}