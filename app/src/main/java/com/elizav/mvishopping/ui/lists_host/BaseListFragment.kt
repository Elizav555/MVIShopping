package com.elizav.mvishopping.ui.lists_host

import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elizav.mvishopping.R
import com.elizav.mvishopping.domain.model.Product
import com.elizav.mvishopping.ui.lists_host.list.ProductAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar

abstract class BaseListFragment(private val clientId: String) : Fragment() {
    open lateinit var productsAdapter: ProductAdapter

    //TODO swipe to refresh
    abstract fun getCurrentProducts(): List<Product>
    abstract var isDesc: Boolean

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

    open fun initAdapter(checkedFunc: ((position: Int) -> Unit)?) = view?.apply {
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

    open fun sortList(products: List<Product>, isDescending: Boolean) {
        productsAdapter.submitList(if (isDescending) {
            products.sortedByDescending { it.name }
        } else {
            products.sortedBy { it.name }
        }
        )
    }
}