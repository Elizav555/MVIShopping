package com.elizav.mvishopping.ui.listsHost

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.elizav.mvishopping.ui.baseList.BaseListFragment
import com.elizav.mvishopping.ui.utils.getFragmentsCollection

class ListsAdapter(fragment: Fragment, private val clientId: String) :
    FragmentStateAdapter(fragment) {
    val fragments = mutableListOf<BaseListFragment>()
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = getFragmentsCollection(clientId = clientId).getOrNull(position)?.listFragment
            ?: throw IllegalArgumentException()
        fragments.add(fragment)
        return fragment
    }
}