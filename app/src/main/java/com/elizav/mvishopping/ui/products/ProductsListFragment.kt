package com.elizav.mvishopping.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elizav.mvishopping.databinding.FragmentProductsListBinding
import com.elizav.mvishopping.domain.model.Product
import com.elizav.mvishopping.ui.lists_host.list.ProductAdapter
import com.elizav.mvishopping.ui.products.state.ProductsListAction
import com.elizav.mvishopping.ui.products.state.ProductsListReducer
import com.elizav.mvishopping.ui.products.state.ProductsListSideEffects
import com.elizav.mvishopping.ui.products.state.ProductsListState
import com.freeletics.rxredux.reduxStore
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@AndroidEntryPoint
class ProductsListFragment(private val clientId: String) : Fragment() {
    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!

    private val actions = BehaviorSubject.create<ProductsListAction>()
    private val compositeDisposable = CompositeDisposable()

    private lateinit var productsAdapter: ProductAdapter

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
        initAdapter()
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

    private fun showLoading(isLoading: Boolean = true) {
        binding.recyclerView.isVisible = !isLoading
        binding.progressBar.isVisible = isLoading
    }

    private fun showSnackbar(text: String) = Snackbar.make(
        binding.root,
        text,
        Snackbar.LENGTH_LONG
    ).show()

    private fun initAdapter() {
        productsAdapter =
            ProductAdapter(::checkedFunc)
        with(binding.recyclerView) {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val dividerItemDecoration = MaterialDividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun updateList(products: List<Product>) {
        productsAdapter.submitList(products)
        binding.tvEmpty.isVisible = products.isEmpty()
    }

    private fun checkedFunc(position: Int) {
        //TODO
    }
}