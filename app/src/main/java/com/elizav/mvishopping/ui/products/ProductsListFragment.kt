package com.elizav.mvishopping.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elizav.mvishopping.R
import com.elizav.mvishopping.databinding.FragmentProductsListBinding
import com.elizav.mvishopping.di.ProductsListSideEffects
import com.elizav.mvishopping.domain.model.ErrorEvent
import com.elizav.mvishopping.ui.baseList.BaseListFragment
import com.elizav.mvishopping.store.listState.ListReducer
import com.elizav.mvishopping.store.listState.ListSideEffects
import com.elizav.mvishopping.ui.dialog.ChangeProductDialog
import com.freeletics.rxredux.reduxStore
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

@AndroidEntryPoint
class ProductsListFragment(clientId: String) : BaseListFragment(clientId) {
    private lateinit var binding: FragmentProductsListBinding

    @Inject
    @ProductsListSideEffects
    lateinit var productsListSideEffects: ListSideEffects

    @Inject
    lateinit var errorEvent: ErrorEvent

    override val listSideEffects: ListSideEffects
        get() = productsListSideEffects

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable += actions.reduxStore(
            currentState,
            productsListSideEffects.sideEffects,
            ListReducer()
        )
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> render(state) }
        initAdapter(::changeProductDialog, ::checkedFunc)
        binding.fabAdd.setOnClickListener {
            changeProductDialog()
        }

        errorEvent.register(this,::handleError)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
        errorEvent.unregister(this)
    }

    private fun checkedFunc(position: Int, isChecked: Boolean) =
        currentState.products?.getOrNull(position)?.let { product ->
            updateProduct(
                product.copy(isPurchased = isChecked)
            )
        }


    private fun changeProductDialog(position: Int? = null) {
        val productOldName = position?.let { currentState.products?.getOrNull(it) }?.name
        ChangeProductDialog(productOldName, position)
            .show(childFragmentManager, getString(R.string.dialog_tag))
    }

    fun changeProductName(newName: String, position: Int?) {
        if (position == null || currentState.products?.getOrNull(position) == null) {
            addProduct(newName)
        } else {
            currentState.products?.getOrNull(position)?.let {
                updateProduct(
                    it.copy(name = newName)
                )
            }
        }
    }
}