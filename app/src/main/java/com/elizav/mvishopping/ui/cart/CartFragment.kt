package com.elizav.mvishopping.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elizav.mvishopping.databinding.FragmentCartBinding
import com.elizav.mvishopping.di.CartSideEffects
import com.elizav.mvishopping.ui.baseList.BaseListFragment
import com.elizav.mvishopping.ui.baseList.state.ListAction
import com.elizav.mvishopping.ui.baseList.state.ListReducer
import com.elizav.mvishopping.ui.baseList.state.ListSideEffects
import com.elizav.mvishopping.ui.baseList.state.ListState
import com.freeletics.rxredux.reduxStore
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment(private val clientId: String) : BaseListFragment(clientId) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    @Inject
    @CartSideEffects
    lateinit var cartSideEffects: ListSideEffects

    override val listSideEffects: ListSideEffects
        get() = cartSideEffects

    override var isDesc: Boolean = false

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
            ListState(clientId),
            listSideEffects.sideEffects,
            ListReducer()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> render(state) }
        initAdapter(null)
        actions.onNext(ListAction.LoadProducts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }
}