package com.elizav.mvishopping.data.mappers

import com.elizav.mvishopping.utils.Constants
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue

fun FirebaseUser.toUser() = mapOf(
    Constants.DISPLAY_NAME to displayName,
    Constants.EMAIL to email,
    Constants.PHOTO_URL to photoUrl?.toString(),
    Constants.CREATED_AT to FieldValue.serverTimestamp()
)