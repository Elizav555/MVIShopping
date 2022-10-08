package com.elizav.mvishopping.ui.lists_host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.elizav.mvishopping.R
import com.elizav.mvishopping.databinding.FragmentListsHostBinding
import com.elizav.mvishopping.utils.fragmentsCollection
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListsHostFragment : Fragment() {
    private var _binding: FragmentListsHostBinding? = null
    private val binding get() = _binding!!

    private lateinit var listsAdapter: ListsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListsHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listsAdapter = ListsAdapter(this)
        binding.viewPager.adapter = listsAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val fragmentParams =
                fragmentsCollection.getOrNull(position) ?: throw IllegalArgumentException()
            tab.text = getString(fragmentParams.labelRes)
            tab.icon = ResourcesCompat.getDrawable(resources, fragmentParams.iconRes, null)
        }.attach()


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort -> true
                    else -> false
                }
            }
        }, viewLifecycleOwner)
        menuHost.invalidateMenu()
    }
}