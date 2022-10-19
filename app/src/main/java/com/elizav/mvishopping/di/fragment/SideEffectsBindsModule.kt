package com.elizav.mvishopping.di.fragment

import com.elizav.mvishopping.di.CartSideEffects
import com.elizav.mvishopping.di.ProductsListSideEffects
import com.elizav.mvishopping.store.listState.ListSideEffects
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import com.elizav.mvishopping.store.cartState.CartSideEffects as CartSideEffectsClass
import com.elizav.mvishopping.store.productsState.ProductsListSideEffects as ProductsListSideEffectsClass

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

