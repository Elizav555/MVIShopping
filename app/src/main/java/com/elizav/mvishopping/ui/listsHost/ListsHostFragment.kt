package com.elizav.mvishopping.ui.listsHost

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
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.elizav.mvishopping.R
import com.elizav.mvishopping.databinding.FragmentListsHostBinding
import com.elizav.mvishopping.ui.baseList.BaseListFragment
import com.elizav.mvishopping.ui.listsHost.state.HostAction
import com.elizav.mvishopping.ui.listsHost.state.HostReducer
import com.elizav.mvishopping.ui.listsHost.state.HostSideEffects
import com.elizav.mvishopping.ui.listsHost.state.HostState
import com.elizav.mvishopping.ui.utils.DialogParams
import com.elizav.mvishopping.ui.utils.ShowDialog.showDialog
import com.elizav.mvishopping.ui.utils.getFragmentsCollection
import com.freeletics.rxredux.reduxStore
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@AndroidEntryPoint
class ListsHostFragment : Fragment() {
    private var _binding: FragmentListsHostBinding? = null
    private val binding get() = _binding!!

    private val actions = BehaviorSubject.create<HostAction>()
    private val compositeDisposable = CompositeDisposable()

    private val args: ListsHostFragmentArgs by navArgs()
    private var sortItem: MenuItem? = null

    @Inject
    lateinit var hostSideEffects: HostSideEffects

    private lateinit var listsAdapter: ListsAdapter
    private val onPageChangeListenerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val currentFragment =
                listsAdapter.fragments.getOrNull(position)
            val isDesc = currentFragment?.currentState?.isDesc ?: false
            sortItem?.icon = ResourcesCompat.getDrawable(
                resources,
                getSortIconResId(isDesc),
                null
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListsHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listsAdapter = ListsAdapter(this, args.clientId)
        binding.viewPager.apply {
            adapter = listsAdapter
            registerOnPageChangeCallback(onPageChangeListenerCallback)
        }


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val fragmentParams =
                getFragmentsCollection(args.clientId).getOrNull(position)
                    ?: throw IllegalArgumentException()
            tab.text = getString(fragmentParams.labelRes)
            tab.icon = ResourcesCompat.getDrawable(resources, fragmentParams.iconRes, null)
        }.attach()

        compositeDisposable += actions.reduxStore(
            HostState(),
            hostSideEffects.sideEffects,
            HostReducer()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> render(state) }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
                sortItem = menu.findItem(R.id.action_sort)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort -> {
                        val currentFragment = childFragmentManager.fragments
                            .find { it is BaseListFragment && it.isResumed } as BaseListFragment?
                        currentFragment?.apply {
                            sortList(!currentState.isDesc)
                            menuItem.setIcon(getSortIconResId(!currentState.isDesc))
                        }
                        return true
                    }
                    R.id.action_logout -> {
                        showLogoutDialog()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
        menuHost.invalidateMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeListenerCallback)
        _binding = null
        compositeDisposable.clear()
    }

    private fun showLogoutDialog() = activity?.let {
        showDialog(it, DialogParams(
            title = getString(R.string.logout),
            message = getString(R.string.message_logout),
            submitBtnText = getString(R.string.yes),
            submitOnClickListener = { _, _ ->
                actions.onNext(HostAction.LogoutAction)
            },
            cancelOnClickListener = { dialog, _ ->
                dialog?.cancel()
            }
        ))
    }

    private fun render(
        state: HostState
    ) {
        when {
            state.isSuccess -> {
                navigateToAuth()
            }
            state.errorMsg != null -> {
                showSnackbar(state.errorMsg)
            }
        }
    }

    private fun navigateToAuth() {
        findNavController().navigate(
            ListsHostFragmentDirections.actionListsHostFragmentToAuthFragment()
        )
    }

    private fun showSnackbar(text: String) = Snackbar.make(
        binding.root,
        text,
        Snackbar.LENGTH_LONG
    ).show()

    private fun getSortIconResId(isDesc: Boolean): Int {
        return if (isDesc) R.drawable.ic_sort_descending_24 else R.drawable.ic_sort_ascending_24
    }
}