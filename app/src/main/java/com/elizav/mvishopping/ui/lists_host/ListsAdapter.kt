package com.elizav.mvishopping.ui.lists_host

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.elizav.mvishopping.utils.getFragmentsCollection

class ListsAdapter(fragment: Fragment, private val clientId: String) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return getFragmentsCollection(clientId = clientId).getOrNull(position)?.fragment
            ?: throw IllegalArgumentException()
    }
}