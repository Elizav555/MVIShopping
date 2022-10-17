package com.elizav.mvishopping.ui.utils

import com.elizav.mvishopping.R
import com.elizav.mvishopping.ui.cart.CartFragment
import com.elizav.mvishopping.ui.products.ProductsListFragment

object FragmentsCollection {
    fun getFragmentsCollection(clientId: String) = mapOf(
        PRODUCTS_LIST_POSITION to FragmentParams(
            R.string.products_list_fragment_label,
            R.drawable.ic_baseline_list_24,
            ProductsListFragment(clientId)
        ),
        CART_POSITION to FragmentParams(
            R.string.cart_fragment_label,
            R.drawable.ic_baseline_shopping_cart_24,
            CartFragment(clientId)
        ),
    )

    const val PRODUCTS_LIST_POSITION = 0
    const val CART_POSITION = 1
}
