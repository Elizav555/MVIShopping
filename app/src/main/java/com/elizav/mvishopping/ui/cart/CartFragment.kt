package com.elizav.mvishopping.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elizav.mvishopping.R
import com.elizav.mvishopping.databinding.FragmentCartBinding
import com.elizav.mvishopping.di.CartSideEffects
import com.elizav.mvishopping.ui.baseList.BaseListFragment
import com.elizav.mvishopping.ui.baseList.state.ListReducer
import com.elizav.mvishopping.ui.baseList.state.ListSideEffects
import com.elizav.mvishopping.ui.utils.DialogParams
import com.elizav.mvishopping.ui.utils.ShowDialog
import com.freeletics.rxredux.reduxStore
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment(clientId: String) : BaseListFragment(clientId) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    @Inject
    @CartSideEffects
    lateinit var cartSideEffects: ListSideEffects

    override val listSideEffects: ListSideEffects
        get() = cartSideEffects

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
            currentState,
            cartSideEffects.sideEffects,
            ListReducer()
        )
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> render(state) }
        initAdapter(null, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }

    override fun deleteProduct(position: Int) {
        currentState.products?.getOrNull(position)?.let { product ->
            activity?.let {
                ShowDialog.showDialog(it, DialogParams(
                    title = getString(R.string.delete_cart),
                    message = getString(R.string.message_delete_cart),
                    submitBtnText = getString(R.string.yes),
                    submitOnClickListener = { _, _ ->
                        updateProduct(
                            product.copy(isPurchased = false)
                        )
                    },
                    cancelOnClickListener = { dialog, _ ->
                        dialog?.cancel()
                        productsAdapter.notifyItemChanged(position)
                    }
                ))
            }
        }
    }
}