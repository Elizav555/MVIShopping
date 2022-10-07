package com.elizav.mvishopping.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.elizav.mvishopping.databinding.FragmentAuthBinding
import com.elizav.mvishopping.ui.auth.state.AuthAction
import com.elizav.mvishopping.ui.auth.state.AuthReducer
import com.elizav.mvishopping.ui.auth.state.AuthSideEffects
import com.elizav.mvishopping.ui.auth.state.AuthState
import com.freeletics.rxredux.reduxStore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val actions = BehaviorSubject.create<AuthAction>()
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var authSideEffects: AuthSideEffects

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                actions.onNext(AuthAction.SignInWithCredAction(credential, account))
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable += actions.reduxStore(
            AuthState(),
            authSideEffects.sideEffects,
            AuthReducer()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> render(state) }

        binding.btnSignIn.setOnClickListener {
            actions.onNext(AuthAction.SignInAction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }

    private fun render(
        state: AuthState
    ) {
        when {
            state.isLoading -> {}//showLoading()
            state.currentClient != null -> {}//navigate()
            state.errorMsg != null -> {}//showError()
            state.beginSignInResult != null -> launcher.launch(
                IntentSenderRequest.Builder(state.beginSignInResult.pendingIntent).build()
            )
        }
    }
}