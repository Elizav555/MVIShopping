package com.elizav.mvishopping.ui.auth.state

import android.content.Context
import android.content.Intent
import com.elizav.mvishopping.domain.auth.AuthRepository
import com.freeletics.rxredux.SideEffect
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.toSingle
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthSideEffects @Inject constructor(
    private val authRepository: AuthRepository,
) {
    val sideEffects = listOf(
        checkAuthSideEffect(),
        signInSideEffect(),
        signInWithCredSideEffect()
    )

    private fun checkAuthSideEffect(): SideEffect<AuthState, AuthAction> = { actions, state ->
        actions.ofType<AuthAction.CheckAuthAction>()
            .switchMap {
                authRepository.currentClient?.toSingle()
                    ?.toObservable()
                    ?.map<AuthAction> { result ->
                        AuthAction.SignedInAction(result.uid)
                    }
                    ?.onErrorReturn { error -> AuthAction.ErrorAction(error.message ?: "") }
                    ?: Single.create { emitter -> emitter.onSuccess(AuthAction.LoadedAction) }
                        .toObservable()
            }
    }

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
                AuthAction.SignedInAction(clientId = result.uid)
            }
            .onErrorReturn { error -> AuthAction.ErrorAction(error.message ?: "") }
    }
}

