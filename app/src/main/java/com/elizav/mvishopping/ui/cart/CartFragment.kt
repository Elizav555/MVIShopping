package com.elizav.mvishopping.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elizav.mvishopping.databinding.FragmentCartBinding
import com.elizav.mvishopping.ui.cart.state.CartAction
import com.elizav.mvishopping.ui.cart.state.CartReducer
import com.elizav.mvishopping.ui.cart.state.CartSideEffects
import com.elizav.mvishopping.ui.cart.state.CartState
import com.elizav.mvishopping.ui.lists_host.BaseListFragment
import com.freeletics.rxredux.reduxStore
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment(private val clientId: String) : BaseListFragment(clientId) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val actions = BehaviorSubject.create<CartAction>()
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var cartSideEffects: CartSideEffects

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable += actions.reduxStore(
            CartState(),
            cartSideEffects.sideEffects,
            CartReducer()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> render(state) }
        initAdapter(null)
        actions.onNext(CartAction.LoadProducts(clientId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }

    private fun render(
        state: CartState
    ) {
        showLoading(state.isLoading)
        when {
            state.products != null -> {
                updateList(state.products)
            }
            state.errorMsg != null -> {
                showSnackbar(state.errorMsg)
            }
        }
    }
}