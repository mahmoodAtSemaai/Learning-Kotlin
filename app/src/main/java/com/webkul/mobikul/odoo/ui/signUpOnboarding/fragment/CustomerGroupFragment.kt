package com.webkul.mobikul.odoo.ui.signUpOnboarding.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.CustomerGroupEntity
import com.webkul.mobikul.odoo.databinding.FragmentCustomerGroupBinding
import com.webkul.mobikul.odoo.databinding.ItemDialogDiscardChangesBinding
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.CustomerGroupAdapter
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.CustomerGroupRecyclerViewClickInterface
import com.webkul.mobikul.odoo.ui.signUpOnboarding.effect.CustomerGroupEffect
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.CustomerGroupIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.UserOnboardingIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.state.CustomerGroupState
import com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel.CustomerGroupViewModel
import com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel.UserOnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CustomerGroupFragment @Inject constructor() :
    BindingBaseFragment<FragmentCustomerGroupBinding>(),
    IView<CustomerGroupIntent, CustomerGroupState, CustomerGroupEffect>,
    CustomerGroupRecyclerViewClickInterface {

    override val layoutId: Int = R.layout.fragment_customer_group
    private val viewModel: CustomerGroupViewModel by viewModels()
    lateinit var userOnboardingViewModel: UserOnboardingViewModel
    private lateinit var dialog: Dialog
    private lateinit var dialogBinding: ItemDialogDiscardChangesBinding

    companion object {
        fun newInstance() = CustomerGroupFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setObservers()
        setListners()

        getUserId()
        setUserOnboardingViewModel()
    }

    private fun setViews(){
        dialog = Dialog(requireContext())
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.item_dialog_discard_changes,
            null,
            false
        )
        dialogBinding.tvLocation.text = requireActivity().applicationContext.getString(R.string.exit_the_app)
        dialogBinding.btnKeepEditing.text = requireActivity().applicationContext.getString(R.string.no)
        dialogBinding.btnDiscard.text = requireActivity().applicationContext.getString(R.string.yes)
    }

    private fun setUserOnboardingViewModel() {
        activity?.let {
            userOnboardingViewModel =
                ViewModelProviders.of(it).get(UserOnboardingViewModel::class.java)
        }
    }

    private fun getUserId() {
        triggerIntent(CustomerGroupIntent.GetUserId)
    }

    private fun setListners() {
        binding.btnContinue.setOnClickListener {
            triggerIntent(CustomerGroupIntent.Continue)
        }

        dialogBinding.btnDiscard.setOnClickListener {
            triggerIntent(CustomerGroupIntent.CloseApp)
        }

        dialogBinding.btnKeepEditing.setOnClickListener {
            dialog.dismiss()
        }

        handleBack()
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

    override fun render(state: CustomerGroupState) {
        when (state) {
            is CustomerGroupState.Idle -> {}
            is CustomerGroupState.Loading -> showProgressDialog()
            is CustomerGroupState.Error -> {
                dismissProgressDialog()
                if(state.failureStatus == FailureStatus.NO_INTERNET){
                    noInternetState()
                }else{
                    showErrorState(state.failureStatus, state.message)
                }
            }
            is CustomerGroupState.FetchCustomerGroups -> {
                dismissProgressDialog()
                getCustomerGroups()
            }
            is CustomerGroupState.GroupSelected -> {
                enableContninueBtn()
            }
            is CustomerGroupState.CustomerGroups -> {
                dismissProgressDialog()
                setUpRecyclerView(state.customerGroupList)
            }
            is CustomerGroupState.CompletedCustomerGroup -> {
                dismissProgressDialog()
                setCompleted()
            }
            is CustomerGroupState.CloseApp -> {
                closeApp()
            }
        }
    }

    private fun closeApp() {
        dialog.dismiss()
        requireActivity().finish()
    }

    private fun noInternetState() {
        triggerActivityIntent(UserOnboardingIntent.Refresh)
    }

    private fun setCompleted() {
        val completedStage = listOf(getString(R.string.group_selection_stage))
        triggerActivityIntent(UserOnboardingIntent.StageCompleted(completedStage))
    }

    private fun getCustomerGroups() {
        triggerIntent(CustomerGroupIntent.FetchCustomer)
    }

    private fun enableContninueBtn() {
        binding.btnContinue.isEnabled = true
    }

    private fun setUpRecyclerView(customerGroups: List<CustomerGroupEntity>) {
        binding.rvCustomerGroups.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            adapter = CustomerGroupAdapter(customerGroups, this@CustomerGroupFragment)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    override fun render(effect: CustomerGroupEffect) {
    }

    override fun triggerIntent(intent: CustomerGroupIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    fun triggerActivityIntent(intent: UserOnboardingIntent) {
        lifecycleScope.launch {
            userOnboardingViewModel.intents.send(intent)
        }
    }

    override fun onItemClick(position: Int, customerGroupsList: List<CustomerGroupEntity>) {
        triggerIntent(
            CustomerGroupIntent.GroupSelected(
                customerGroupsList[position].id.toString(),
                customerGroupsList[position].name
            )
        )
    }

    private fun handleBack(){
        activity?.onBackPressedDispatcher?.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showDialog()
                }
            })
    }

    fun showDialog() {
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }
}