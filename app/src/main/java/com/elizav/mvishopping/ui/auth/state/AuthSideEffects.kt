package com.elizav.mvishopping.ui.auth.state

import android.content.Context
import android.content.Intent
import com.elizav.mvishopping.R
import com.elizav.mvishopping.domain.auth.AuthRepository
import com.freeletics.rxredux.SideEffect
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthSideEffects @Inject constructor(
    private val authRepository: AuthRepository,
    private val context: Context
) {
    val sideEffects = listOf(
        signInSideEffect(),
        signInWithCredSideEffect()
    )

    private fun signInSideEffect(): SideEffect<AuthState, AuthAction> = { actions, state ->
        actions.ofType<AuthAction.SignInAction>()
            .switchMap {
                oneTapSignIn()
            }
    }

    private fun oneTapSignIn(): Observable<AuthAction> {
        return authRepository.oneTapSignInWithGoogle()
            .subscribeOn(Schedulers.io())
            .toObservable()
            .map<AuthAction> { result ->
                AuthAction.BeginSignInResultAction(result)
            }
            .onErrorReturn { error -> AuthAction.ErrorAction(error.message ?: "") }
            .startWith(AuthAction.LoadingAction)
    }

    private fun signInWithCredSideEffect(): SideEffect<AuthState, AuthAction> = { actions, state ->
        actions
            .ofType(AuthAction.SignInWithCredAction::class.java)
            .switchMap {
                signIn(it.intentData)
            }
    }

    private fun signIn(intentData: Intent): Observable<AuthAction> {
        return authRepository.firebaseSignInWithGoogle(intentData)
            .subscribeOn(Schedulers.io())
            .toObservable()
            .map<AuthAction> { result ->
                if (result) {
                    authRepository.currentClient?.let {
                        AuthAction.SignedInAction(clientId = it.uid)
                    } ?: AuthAction.ErrorAction(context.getString(R.string.error))
                } else {
                    AuthAction.ErrorAction(context.getString(R.string.error))
                }
            }
            .onErrorReturn { error -> AuthAction.ErrorAction(error.message ?: "") }
            .startWith(AuthAction.LoadingAction)
    }
}

