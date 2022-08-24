package com.webkul.mobikul.odoo.ui.account.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.AccountInfoEntity
import com.webkul.mobikul.odoo.data.request.UserDetailsRequest
import com.webkul.mobikul.odoo.databinding.FragmentAccountInfoV1Binding
import com.webkul.mobikul.odoo.databinding.ItemDialogDiscardChangesBinding
import com.webkul.mobikul.odoo.helper.FragmentHelper.replaceFragment
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import com.webkul.mobikul.odoo.ui.account.effect.AccountInfoEffect
import com.webkul.mobikul.odoo.ui.account.intent.AccountInfoIntent
import com.webkul.mobikul.odoo.ui.account.state.AccountInfoState
import com.webkul.mobikul.odoo.ui.account.viewModel.AccountInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AccountInfoV1Fragment @Inject constructor() :
    BindingBaseFragment<FragmentAccountInfoV1Binding>(),
    IView<AccountInfoIntent, AccountInfoState, AccountInfoEffect> {

    override val layoutId: Int = R.layout.fragment_account_info_v1
    private val viewModel: AccountInfoViewModel by viewModels()
    private lateinit var dialog: Dialog
    private lateinit var dialogBinding: ItemDialogDiscardChangesBinding

    companion object {
        fun newInstance(
            accountInfoEntity: AccountInfoEntity
        ) = AccountInfoV1Fragment().also { accountInfoFragment ->
            accountInfoFragment.arguments = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_ACCOUNT_INFO_DATA, accountInfoEntity)
            }
        }

        fun newInstance(): AccountInfoV1Fragment {
            return AccountInfoV1Fragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setObservers()
        setListners()
        getUserIdCustomerId()
    }

    private fun initViews() {
        triggerIntent(AccountInfoIntent.InitViews)
    }

    private fun getArgs() {
        triggerIntent(AccountInfoIntent.GetArgs(arguments))
    }

    private fun setViews() {
        triggerIntent(AccountInfoIntent.SetViews)
    }

    private fun getUserIdCustomerId() {
        triggerIntent(AccountInfoIntent.GetUserIdCustomerId)
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
        binding.ivEditName.setOnClickListener {
            triggerIntent(
                AccountInfoIntent.EditName(
                    binding.tvCustomerName.text.toString(),
                    getString(R.string.change_name)
                )
            )
        }

        binding.ivEditGroupName.setOnClickListener {
            triggerIntent(
                AccountInfoIntent.EditGroupName(
                    binding.tvCustomerGroupName.text.toString(),
                    getString(R.string.change_group_name)
                )
            )
        }

        binding.btnContinue.setOnClickListener {
            triggerIntent(
                AccountInfoIntent.Save(
                    UserDetailsRequest(
                        name = binding.tvCustomerName.text.toString(),
                        groupName = binding.tvCustomerGroupName.text.toString(),
                        isOnboarding = false
                    )
                )
            )
        }

        dialogBinding.btnDiscard.setOnClickListener {
            triggerIntent(AccountInfoIntent.Discard)
        }

        dialogBinding.btnKeepEditing.setOnClickListener {
            triggerIntent(AccountInfoIntent.KeepEditing)
        }

        handleBack()
    }

    override fun render(state: AccountInfoState) {
        when (state) {
            is AccountInfoState.SetData -> setViews(state.accountInfoEntity)
            is AccountInfoState.ShowDialog -> showDialog()
            is AccountInfoState.KeepEditing -> dialog.dismiss()
            is AccountInfoState.EnableContinue -> enableConitnue()
            is AccountInfoState.DisableContinue -> disableConitnue()
            is AccountInfoState.GetArgs -> getArgs()
            is AccountInfoState.Error -> showErrorState(state.failureStatus,state.message)
            is AccountInfoState.InitViews -> setDialogViews()
        }
    }

    private fun setDialogViews() {
        dialog = Dialog(requireContext())
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.item_dialog_discard_changes,
            null,
            false
        )
        setWindowColor()
        setViews()
    }

    private fun enableConitnue(){
        binding.btnContinue.isEnabled = true
    }

    private fun disableConitnue(){
        binding.btnContinue.isEnabled = false
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

    private fun handleBack() {
        binding.toolbar.setNavigationOnClickListener {
            triggerIntent(
                AccountInfoIntent.BackNavigation
            )
        }

        activity?.onBackPressedDispatcher?.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    triggerIntent(
                        AccountInfoIntent.BackNavigation
                    )
                }
            })
    }

    override fun render(effect: AccountInfoEffect) {
        when (effect) {
            is AccountInfoEffect.NavigateToEditAccountInfo -> navigateToEditAccountInfo(
                effect.value,
                effect.title,
                effect.isName
            )
            is AccountInfoEffect.NavigateToAccount -> navigateToAccount(effect.homePageResponse)
        }
    }

    override fun triggerIntent(intent: AccountInfoIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun setViews(accountInfoEntity: AccountInfoEntity) {
        if(accountInfoEntity.groupName == requireActivity().applicationContext.getString(R.string.farmers) || accountInfoEntity.groupName == requireActivity().applicationContext.getString(R.string.others)){
            binding.clGroupName.makeGone()
            binding.tvCustomerName.text = accountInfoEntity.name
        } else {
            binding.tvCustomerName.text = accountInfoEntity.userName
        }
        binding.tvCustomerCategory.text = accountInfoEntity.groupName
        binding.tvCustomerGroupName.text = accountInfoEntity.customerGroupName
        binding.tvCustomerPhoneNumber.text = accountInfoEntity.phoneNumber
        triggerIntent(AccountInfoIntent.EditValue)
    }

    private fun setWindowColor(){
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.background_appbar_color)
        }
    }

    private fun navigateToEditAccountInfo(value: AccountInfoEntity, title: String, isName: Boolean) {
        replaceFragment(
            R.id.container,
            requireActivity(),
            EditAccountInfoFragment.newInstance(
                value, title, isName
            ),
            EditAccountInfoFragment::class.java.simpleName,
            true,
            true
        )
    }

    private fun navigateToAccount(homePageResponse: HomePageResponse) {
        requireActivity().finish()
    }
}