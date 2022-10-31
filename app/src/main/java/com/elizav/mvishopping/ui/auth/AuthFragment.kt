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
import com.elizav.mvishopping.rx.CacheRelay
import com.elizav.mvishopping.store.authState.AuthAction
import com.elizav.mvishopping.store.authState.AuthEffect
import com.elizav.mvishopping.store.authState.AuthEffect.ShowError
import com.elizav.mvishopping.store.authState.AuthState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var statesObservable: Observable<AuthState>

    @Inject
    lateinit var actionsRelay: CacheRelay<AuthAction>

    @Inject
    lateinit var effects: CacheRelay<AuthEffect>

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            it.data?.let { data -> actionsRelay.accept(AuthAction.SignInWithCredAction(data)) }
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
        compositeDisposable += statesObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> render(state) }
        compositeDisposable += effects
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { effect -> handleEffect(effect) }
        actionsRelay.accept(AuthAction.CheckAuthAction)
        binding.btnSignIn.setOnClickListener {
            actionsRelay.accept(AuthAction.SignInAction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    private fun render(state: AuthState) {
        showLoading(state.isLoading)
    }

    private fun handleEffect(effect: AuthEffect) {
        when (effect) {
            is ShowError -> showSnackbar(effect.text)
            is AuthEffect.LaunchIntent -> {
                launcher.launch(
                    IntentSenderRequest.Builder(effect.signInResult.pendingIntent).build()
                )
            }
            is AuthEffect.NavigateToList -> navigateToList(effect.clientId)
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