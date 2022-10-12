package com.elizav.mvishopping.ui.baseList

import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elizav.mvishopping.R
import com.elizav.mvishopping.domain.model.Product
import com.elizav.mvishopping.ui.baseList.list.ProductAdapter
import com.elizav.mvishopping.ui.baseList.state.ListAction
import com.elizav.mvishopping.ui.baseList.state.ListSideEffects
import com.elizav.mvishopping.ui.baseList.state.ListState
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseListFragment(private val clientId: String) : Fragment() {
    open lateinit var productsAdapter: ProductAdapter

    //TODO swipe to refresh
    abstract var isDesc: Boolean

    open val actions: BehaviorSubject<ListAction> = BehaviorSubject.create<ListAction>()
    open val compositeDisposable = CompositeDisposable()

    abstract val listSideEffects: ListSideEffects

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    open fun render(state: ListState) {
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

    open fun showLoading(isLoading: Boolean = true) {
        view?.apply {
            findViewById<RecyclerView>(R.id.recycler_view).isVisible = !isLoading
            findViewById<ProgressBar>(R.id.progressBar).isVisible = isLoading
        }
    }

    open fun showSnackbar(text: String) = view?.let {
        Snackbar.make(
            it,
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }

    open fun initAdapter(checkedFunc: ((position: Int, isChecked: Boolean, product: Product) -> Unit)?) = view?.apply {
        productsAdapter =
            ProductAdapter(checkedFunc)
        with(findViewById<RecyclerView>(R.id.recycler_view)) {
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

    open fun updateList(products: List<Product>) = view?.let {
        productsAdapter.submitList(products)
        it.findViewById<TextView>(R.id.tv_empty).isVisible = products.isEmpty()
    }

    open fun sortList() {
        actions.onNext(ListAction.SortAction(isDesc))
    }

    open fun updateProduct(productPosition:Int, updatedProduct: Product){
        actions.onNext(ListAction.UpdateProductAction(productPosition, updatedProduct))
    }
}