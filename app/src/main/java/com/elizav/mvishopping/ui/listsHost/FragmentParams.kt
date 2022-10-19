package com.elizav.mvishopping.ui.listsHost

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.elizav.mvishopping.ui.baseList.BaseListFragment

data class FragmentParams(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
    val listFragment: BaseListFragment
)