package com.elizav.mvishopping.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class SignInRequest

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class SignUpRequest

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CartSideEffects

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ProductsListSideEffects