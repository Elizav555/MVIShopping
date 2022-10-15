package com.elizav.mvishopping.ui.utils

import com.elizav.mvishopping.domain.model.Product

object ProductListExtension {
    fun List<Product>.sortByName(isDesc: Boolean) = if (isDesc) {
        this.sortedByDescending { it.name }
    } else {
        this.sortedBy { it.name }
    }
}