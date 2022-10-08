package com.elizav.mvishopping.ui.lists_host

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.elizav.mvishopping.utils.fragmentsCollection

class ListsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return fragmentsCollection.getOrNull(position)?.fragment ?: throw IllegalArgumentException()
    }
}