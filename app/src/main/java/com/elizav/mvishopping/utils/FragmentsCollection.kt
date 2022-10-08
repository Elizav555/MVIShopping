package com.elizav.mvishopping.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.elizav.mvishopping.R
import com.elizav.mvishopping.ui.cart.CartFragment
import com.elizav.mvishopping.ui.products.ProductsListFragment

val fragmentsCollection = listOf(
    FragmentParams(
        R.string.products_list_fragment_label,
        R.drawable.ic_baseline_list_24,
        ProductsListFragment()
    ),
    FragmentParams(
        R.string.cart_fragment_label,
        R.drawable.ic_baseline_shopping_cart_24,
        CartFragment()
    ),
)

data class FragmentParams(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
    val fragment: Fragment
)