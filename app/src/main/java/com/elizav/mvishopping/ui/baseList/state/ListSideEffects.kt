package com.elizav.mvishopping.ui.baseList.state

import com.freeletics.rxredux.SideEffect

interface ListSideEffects {
    val sideEffects: List<SideEffect<ListState, ListAction>>

    fun loadProductsSideEffect(): SideEffect<ListState, ListAction>

    fun sortProductsSideEffect(): SideEffect<ListState, ListAction>

    fun updateProductSideEffect(): SideEffect<ListState, ListAction>
}