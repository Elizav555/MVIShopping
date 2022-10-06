package com.elizav.mvishopping.data.auth

import com.elizav.mvishopping.data.mappers.toUser
import com.elizav.mvishopping.domain.auth.AuthRepository
import com.elizav.mvishopping.utils.Constants.CREATED_AT
import com.elizav.mvishopping.utils.Constants.DISPLAY_NAME
import com.elizav.mvishopping.utils.Constants.EMAIL
import com.elizav.mvishopping.utils.Constants.PHOTO_URL
import com.elizav.mvishopping.utils.Constants.SIGN_IN_REQUEST
import com.elizav.mvishopping.utils.Constants.SIGN_UP_REQUEST
import com.elizav.mvishopping.utils.Constants.USERS
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val db: FirebaseFirestore
) : AuthRepository {
    override val isUserAuthenticated = auth.currentUser != null

    override fun oneTapSignInWithGoogle(): Single<BeginSignInResult> = Single.create { emitter ->
        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener {
            emitter.onSuccess(it)
        }.addOnFailureListener {
            oneTapClient.beginSignIn(signUpRequest).addOnSuccessListener {
                emitter.onSuccess(it)
            }.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }

    override fun firebaseSignInWithGoogle(
        googleCredential: AuthCredential
    ): Single<Boolean> = Single.create { emitter ->
        auth.signInWithCredential(googleCredential).addOnSuccessListener {
            val isNewUser = it.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                auth.currentUser?.apply {
                    val user = toUser()
                    db.collection(USERS).document(uid).set(user).addOnSuccessListener {
                        emitter.onSuccess(true)
                    }.addOnFailureListener { ex ->
                        emitter.onError(ex)
                    }
                }
            }
        }.addOnFailureListener {
            emitter.onError(it)
        }
    }
}
