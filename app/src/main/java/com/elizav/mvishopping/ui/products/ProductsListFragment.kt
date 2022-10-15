package com.elizav.mvishopping.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elizav.mvishopping.R
import com.elizav.mvishopping.databinding.FragmentProductsListBinding
import com.elizav.mvishopping.di.ProductsListSideEffects
import com.elizav.mvishopping.domain.model.Product
import com.elizav.mvishopping.ui.baseList.BaseListFragment
import com.elizav.mvishopping.ui.baseList.state.ListReducer
import com.elizav.mvishopping.ui.baseList.state.ListSideEffects
import com.elizav.mvishopping.ui.products.dialog.ChangeProductDialog
import com.freeletics.rxredux.reduxStore
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

@AndroidEntryPoint
class ProductsListFragment(clientId: String) : BaseListFragment(clientId) {
    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!

    @Inject
    @ProductsListSideEffects
    lateinit var productsListSideEffects: ListSideEffects

    override val listSideEffects: ListSideEffects
        get() = productsListSideEffects

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable += actions.reduxStore(
            currentState,
            productsListSideEffects.sideEffects,
            ListReducer()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> render(state) }
        initAdapter(::changeProductDialog, ::checkedFunc)
        binding.fabAdd.setOnClickListener {
            changeProductDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
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
            val id = currentState.products?.size ?: 0
            updateProduct(
                 Product(
                    id = id,
                    name = newName,
                    isPurchased = false
                )
            )
        } else {
            currentState.products?.getOrNull(position)?.let {
                updateProduct(
                    it.copy(name = newName)
                )
            }
        }
    }
}