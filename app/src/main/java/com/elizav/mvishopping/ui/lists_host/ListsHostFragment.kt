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
import androidx.navigation.fragment.navArgs
import com.elizav.mvishopping.R
import com.elizav.mvishopping.databinding.FragmentListsHostBinding
import com.elizav.mvishopping.ui.lists_host.state.HostAction
import com.elizav.mvishopping.ui.lists_host.state.HostReducer
import com.elizav.mvishopping.ui.lists_host.state.HostSideEffects
import com.elizav.mvishopping.ui.lists_host.state.HostState
import com.elizav.mvishopping.utils.getFragmentsCollection
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

    @Inject
    lateinit var hostSideEffects: HostSideEffects

    private lateinit var listsAdapter: ListsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListsHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listsAdapter = ListsAdapter(this, args.clientId)
        binding.viewPager.adapter = listsAdapter

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
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort -> true
                    R.id.action_logout -> {
                        //TODO ask before
                        actions.onNext(HostAction.LogoutAction)
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
        _binding = null
        compositeDisposable.clear()
    }

    private fun render(
        state: HostState
    ) {
        when {
            state.isSuccess -> {
                navigateToList()
            }
            state.errorMsg != null -> {
                showSnackbar(state.errorMsg)
            }
        }
    }

    private fun navigateToList() {
        findNavController().navigate(
            ListsHostFragmentDirections.actionListsHostFragmentToAuthFragment()
        )
    }

    private fun showSnackbar(text: String) = Snackbar.make(
        binding.root,
        text,
        Snackbar.LENGTH_LONG
    ).show()
}