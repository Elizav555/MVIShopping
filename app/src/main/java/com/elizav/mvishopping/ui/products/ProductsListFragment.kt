package com.elizav.mvishopping.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elizav.mvishopping.databinding.FragmentProductsListBinding
import com.elizav.mvishopping.ui.lists_host.BaseListFragment
import com.elizav.mvishopping.ui.products.state.ProductsListAction
import com.elizav.mvishopping.ui.products.state.ProductsListReducer
import com.elizav.mvishopping.ui.products.state.ProductsListSideEffects
import com.elizav.mvishopping.ui.products.state.ProductsListState
import com.freeletics.rxredux.reduxStore
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@AndroidEntryPoint
class ProductsListFragment(private val clientId: String) : BaseListFragment(clientId) {
    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!

    private val actions = BehaviorSubject.create<ProductsListAction>()
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var productsListSideEffects: ProductsListSideEffects

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
            ProductsListState(),
            productsListSideEffects.sideEffects,
            ProductsListReducer()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> render(state) }
        initAdapter(::checkedFunc)
        actions.onNext(ProductsListAction.LoadProducts(clientId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }

    private fun render(
        state: ProductsListState
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

    private fun checkedFunc(position: Int) {
        //TODO
    }
}