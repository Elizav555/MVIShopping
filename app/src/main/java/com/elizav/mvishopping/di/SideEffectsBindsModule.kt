package com.elizav.mvishopping.di

import com.elizav.mvishopping.ui.baseList.state.ListSideEffects
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import com.elizav.mvishopping.ui.cart.state.CartSideEffects as CartSideEffectsClass
import com.elizav.mvishopping.ui.products.state.ProductsListSideEffects as ProductsListSideEffectsClass

@Module
@InstallIn(FragmentComponent::class)
abstract class SideEffectsBindsModule {
    @Binds
    @CartSideEffects
    abstract fun bindCartSideEffects(
        cartSideEffects: CartSideEffectsClass
    ): ListSideEffects

    @Binds
    @ProductsListSideEffects
    abstract fun bindProductsListSideEffects(
        productsListSideEffects: ProductsListSideEffectsClass
    ): ListSideEffects
}

