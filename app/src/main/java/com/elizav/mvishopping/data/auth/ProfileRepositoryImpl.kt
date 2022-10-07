package com.elizav.mvishopping.data.auth

import com.elizav.mvishopping.domain.auth.ProfileRepository
import com.elizav.mvishopping.utils.Constants.USERS
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    private var signInClient: GoogleSignInClient,
    private val db: FirebaseFirestore
) : ProfileRepository {
    override val displayName = auth.currentUser?.displayName.toString()
    override val photoUrl = auth.currentUser?.photoUrl.toString()

    override fun signOut(): Single<Boolean> = Single.create { emitter ->
        oneTapClient.signOut().addOnSuccessListener {
            auth.signOut()
            emitter.onSuccess(true)
        }.addOnFailureListener {
            emitter.onError(it)
        }
    }

    //TODO ask about this
    override fun revokeAccess(): Single<Boolean> = Single.create { emitter ->
        auth.currentUser?.apply {
            deleteUser(uid).andThen {
                revoke().andThen {
                    signOutClient().andThen {
                        delete().addOnSuccessListener {
                            emitter.onSuccess(true)
                        }.addOnFailureListener {
                            emitter.onError(it)
                        }
                    }
                }
            }
        }
    }

    private fun deleteUser(uid: String) = Completable.create { emitter ->
        db.collection(USERS).document(uid).delete().addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.onError(it) }
    }

    private fun revoke() = Completable.create { emitter ->
        signInClient.revokeAccess().addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.onError(it) }
    }

    private fun signOutClient() = Completable.create { emitter ->
        oneTapClient.signOut().addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.onError(it) }
    }
}

