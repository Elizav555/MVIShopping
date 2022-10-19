package com.elizav.mvishopping.data.auth

import android.content.Intent
import android.util.Log
import com.elizav.mvishopping.data.client.ClientsRepositoryImpl.Companion.CLIENTS
import com.elizav.mvishopping.data.client.ClientsRepositoryImpl.Companion.NAME
import com.elizav.mvishopping.di.SignInRequest
import com.elizav.mvishopping.di.SignUpRequest
import com.elizav.mvishopping.domain.auth.AuthRepository
import com.elizav.mvishopping.domain.model.AppException
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    @SignInRequest
    private val signInRequest: BeginSignInRequest,
    @SignUpRequest
    private val signUpRequest: BeginSignInRequest,
    private val db: FirebaseFirestore
) : AuthRepository {
    override val currentClient: FirebaseUser? get() = auth.currentUser
    override val isUserAuthenticated get() = auth.currentUser != null

    private val signOutSubject = PublishSubject.create<Unit>()

    private val currentAuthStateObserver = Observable.create<Boolean> { emitter ->
        AuthStateListener { emitter.onNext(it.currentUser != null) }
            .apply {
                auth.addAuthStateListener(this)
                emitter.setCancellable { auth.removeAuthStateListener(this) }
            }
    }
        .share()
        .takeUntil(signOutSubject)

    override fun observeAuthState(): Observable<Boolean> = currentAuthStateObserver


    override fun oneTapSignInWithGoogle(): Single<BeginSignInResult> = Single.create { emitter ->
        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener {
            emitter.onSuccess(it)
        }.addOnFailureListener { ex ->
            if (ex is ApiException) {
                Log.e("signUpFail", ex.message ?: "")
                oneTapClient.beginSignIn(signUpRequest).addOnSuccessListener { beginSignInRes ->
                    emitter.onSuccess(beginSignInRes)
                }.addOnFailureListener { e ->
                    emitter.onError(e)
                }
            } else {
                emitter.onError(ex)
            }
        }
    }

    override fun firebaseSignInWithGoogle(
        intentData: Intent
    ): Single<FirebaseUser> = Single.create { emitter ->
        val credential = oneTapClient.getSignInCredentialFromIntent(intentData)
        credential.googleIdToken?.let { idToken ->
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(firebaseCredential).addOnSuccessListener { authResult ->
                val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
                if (isNewUser) {
                    auth.currentUser?.apply {
                        db.collection(CLIENTS).document(uid).set(mapOf(NAME to displayName))
                            .addOnSuccessListener {
                                emitter.onSuccess(this)
                            }.addOnFailureListener { ex ->
                                emitter.onError(ex)
                            }
                    }
                } else {
                    auth.currentUser?.let { emitter.onSuccess(it) }
                        ?: emitter.onError(AppException.AuthErrorException())
                }
            }.addOnFailureListener {
                emitter.onError(it)
            }
        } ?: emitter.onError(AppException.AuthErrorException())
    }

    override fun signOut(): Single<Boolean> = Single.create { emitter ->
        oneTapClient.signOut().addOnSuccessListener {
            signOutSubject.onNext(Unit)
            auth.signOut()
            emitter.onSuccess(true)
        }.addOnFailureListener {
            emitter.onError(it)
        }
    }
}
