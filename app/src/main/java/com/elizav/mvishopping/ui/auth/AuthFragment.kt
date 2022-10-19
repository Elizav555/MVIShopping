package com.elizav.mvishopping.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.elizav.mvishopping.R
import com.elizav.mvishopping.databinding.FragmentAuthBinding
import com.elizav.mvishopping.store.authState.AuthAction
import com.elizav.mvishopping.store.authState.AuthReducer
import com.elizav.mvishopping.store.authState.AuthSideEffects
import com.elizav.mvishopping.store.authState.AuthState
import com.freeletics.rxredux.reduxStore
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding

    private val actions = BehaviorSubject.create<AuthAction>()
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var authSideEffects: AuthSideEffects

    @Inject
    lateinit var authState: AuthState

    @Inject
    lateinit var authReducer: AuthReducer

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            it.data?.let { data -> actions.onNext(AuthAction.SignInWithCredAction(data)) }
                ?: showSnackbar(getString(R.string.error))
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable += actions.reduxStore(
            authState,
            authSideEffects.sideEffects,
            authReducer
        )
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> render(state) }
        actions.onNext(AuthAction.CheckAuthAction)
        binding.btnSignIn.setOnClickListener {
            actions.onNext(AuthAction.SignInAction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    private fun render(
        state: AuthState
    ) {
        showLoading(state.isLoading)
        when {
            state.currentClientId != null -> {
                navigateToList(state.currentClientId)
            }
            state.errorMsg != null -> {
                showLoading(false)
                showSnackbar(state.errorMsg)
            }
            state.beginSignInResult != null -> {
                launcher.launch(
                    IntentSenderRequest.Builder(state.beginSignInResult.pendingIntent).build()
                )
            }
        }
    }

    private fun navigateToList(clientId: String) {
        findNavController().navigate(
            AuthFragmentDirections.actionAuthFragmentToListsHostFragment(clientId)
        )
    }

    private fun showLoading(isLoading: Boolean = true) {
        binding.btnSignIn.isVisible = !isLoading
        binding.progressBar.isVisible = isLoading
    }

    private fun showSnackbar(text: String) = Snackbar.make(
        binding.root,
        text,
        Snackbar.LENGTH_LONG
    ).show()
}