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
import io.reactivex.Observable
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
    override val currentClient: FirebaseUser? get() = auth.currentUser
    override val isUserAuthenticated get() = auth.currentUser != null

    private var authStateListener: FirebaseAuth.AuthStateListener? = null

    override fun observeAuthState(): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            authStateListener = FirebaseAuth.AuthStateListener {
                if (!emitter.isDisposed) emitter.onNext(it.currentUser != null)
            }.apply {
                auth.addAuthStateListener(this)
                emitter.setCancellable {
                    auth.removeAuthStateListener(this)
                }
            }
        }
    }

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
    ): Single<FirebaseUser> = Single.create { emitter ->
        val credential = oneTapClient.getSignInCredentialFromIntent(intentData)
        credential.googleIdToken?.let { idToken ->
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(firebaseCredential).addOnSuccessListener {
                val isNewUser = it.additionalUserInfo?.isNewUser ?: false
                if (isNewUser) {
                    auth.currentUser?.apply {
                        db.collection(CLIENTS).document(uid).set(mapOf(NAME to displayName))
                            .addOnSuccessListener {
                                emitter.onSuccess(this)
                            }.addOnFailureListener { ex ->
                                emitter.onError(ex)
                            }
                    }
                } else auth.currentUser?.let { emitter.onSuccess(it) }
                    ?: emitter.onError(Exception())
            }.addOnFailureListener {
                emitter.onError(it)
            }
        } ?: emitter.onError(Exception())
    }

    override fun signOut(): Single<Boolean> = Single.create { emitter ->
        oneTapClient.signOut().addOnSuccessListener {
            authStateListener?.let { auth.removeAuthStateListener(it) }
            auth.signOut()
            emitter.onSuccess(true)
        }.addOnFailureListener {
            emitter.onError(it)
        }
    }
}
