package com.elizav.mvishopping.data.auth

import android.content.Intent
import com.elizav.mvishopping.di.SignInRequest
import com.elizav.mvishopping.di.SignUpRequest
import com.elizav.mvishopping.domain.auth.AuthRepository
import com.elizav.mvishopping.utils.Constants.CLIENTS
import com.elizav.mvishopping.utils.Constants.NAME
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @SignInRequest
    private var signInRequest: BeginSignInRequest,
    @SignUpRequest
    private var signUpRequest: BeginSignInRequest,
    private val db: FirebaseFirestore
) : AuthRepository {
    override val currentClient: FirebaseUser? = auth.currentUser
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
        intentData: Intent
    ): Single<Boolean> = Single.create { emitter ->
        val credential = oneTapClient.getSignInCredentialFromIntent(intentData)
        credential.googleIdToken?.let { idToken ->
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(firebaseCredential).addOnSuccessListener {
                val isNewUser = it.additionalUserInfo?.isNewUser ?: false
                if (isNewUser) {
                    auth.currentUser?.apply {
                        db.collection(CLIENTS).document(uid).set(mapOf(NAME to displayName))
                            .addOnSuccessListener {
                                emitter.onSuccess(true)
                            }.addOnFailureListener { ex ->
                                emitter.onError(ex)
                            }
                    }
                } else emitter.onSuccess(true)
            }.addOnFailureListener {
                emitter.onError(it)
            }
        } ?: emitter.onError(Exception())
    }
}
