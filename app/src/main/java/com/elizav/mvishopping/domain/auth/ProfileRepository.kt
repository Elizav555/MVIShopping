package com.elizav.mvishopping.domain.auth

import io.reactivex.Single

interface ProfileRepository {
    val displayName: String
    val photoUrl: String

    fun signOut(): Single<Boolean>

    fun revokeAccess(): Single<Boolean>
}