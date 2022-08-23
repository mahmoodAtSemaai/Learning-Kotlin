package com.webkul.mobikul.odoo.ui.signUpOnboarding.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentNoInternetBinding
import com.webkul.mobikul.odoo.ui.signUpOnboarding.effect.NoInternetEffect
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.NoInternetIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.state.NoInternetState
import com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel.NoInternetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NoInternetFragment @Inject constructor(private val noInternetStateListner: NoInternetStateListner) :
    BindingBaseFragment<FragmentNoInternetBinding>(),
    IView<NoInternetIntent, NoInternetState, NoInternetEffect> {

    companion object {
        fun newInstance(listner : NoInternetStateListner) = NoInternetFragment(listner)
    }

    override val layoutId: Int = R.layout.fragment_no_internet
    private val viewModel: NoInternetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListners()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.state.collect {
                render(it)
            }
        }

        lifecycleScope.launch {
            viewModel.effect.collect {
                render(it)
            }
        }
    }

    private fun setListners() {
        binding.btnRefresh.setOnClickListener {
            triggerIntent(NoInternetIntent.Refresh)
        }
    }

    override fun render(state: NoInternetState) {
        when(state){
            is NoInternetState.Refresh -> refresh()
            is NoInternetState.Idle -> {}
        }
    }

    private fun refresh() {
        /*if(requireActivity().supportFragmentManager.backStackEntryCount > 0){
            requireActivity().supportFragmentManager.popBackStack()
        }else{
            noInternetStateListner.noInternetListner()
        }*/
        noInternetStateListner.noInternetListner()
    }

    override fun render(effect: NoInternetEffect) {}

    override fun triggerIntent(intent: NoInternetIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }
}

interface NoInternetStateListner {
    fun noInternetListner()
}