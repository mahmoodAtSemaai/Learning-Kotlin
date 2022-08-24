package com.webkul.mobikul.odoo.ui.account.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.AccountInfoEntity
import com.webkul.mobikul.odoo.databinding.FragmentEditAccountInfoBinding
import com.webkul.mobikul.odoo.databinding.ItemDialogDiscardChangesBinding
import com.webkul.mobikul.odoo.helper.FragmentHelper.replaceFragment
import com.webkul.mobikul.odoo.ui.account.effect.EditAccountInfoEffect
import com.webkul.mobikul.odoo.ui.account.intent.EditAccountInfoIntent
import com.webkul.mobikul.odoo.ui.account.state.EditAccountInfoState
import com.webkul.mobikul.odoo.ui.account.viewModel.EditAccountInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditAccountInfoFragment @Inject constructor() :
    BindingBaseFragment<FragmentEditAccountInfoBinding>(),
    IView<EditAccountInfoIntent, EditAccountInfoState, EditAccountInfoEffect> {

    override val layoutId: Int = R.layout.fragment_edit_account_info
    private val viewModel: EditAccountInfoViewModel by viewModels()
    private lateinit var dialog: Dialog
    private lateinit var dialogBinding: ItemDialogDiscardChangesBinding

    companion object {
        fun newInstance(
            value : AccountInfoEntity,
            toolBarTitle: String,
            isName: Boolean
        ) = EditAccountInfoFragment().also { editAccountInfoFragment ->
            editAccountInfoFragment.arguments = Bundle().apply {
                putString(BundleConstant.BUNDLE_KEY_TITLE, toolBarTitle)
                putBoolean(BundleConstant.BUNDLE_KEY_IS_USER_NAME_EDIT, isName)
                putParcelable(BundleConstant.BUNDLE_KEY_ACCOUNT_INFO_DATA,value)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        getArgs()
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

    private fun getArgs() {
        triggerIntent(EditAccountInfoIntent.GetArgs(arguments))
    }

    private fun handleBack() {
        binding.toolbar.setNavigationOnClickListener {
            triggerIntent(EditAccountInfoIntent.BackNavigation(binding.etChangeFeild.text.toString()))
        }

        activity?.onBackPressedDispatcher?.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    triggerIntent(EditAccountInfoIntent.BackNavigation(binding.etChangeFeild.text.toString()))
                }
            })
    }

    private fun setListners() {
        binding.etChangeFeild.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                triggerIntent(EditAccountInfoIntent.EditValue(s.toString()))
            }
        })

        dialogBinding.btnDiscard.setOnClickListener {
            triggerIntent(EditAccountInfoIntent.Discard)
        }

        dialogBinding.btnKeepEditing.setOnClickListener {
            triggerIntent(EditAccountInfoIntent.KeepEditing)
        }

        binding.btnContinue.setOnClickListener {
            triggerIntent(EditAccountInfoIntent.Save(binding.etChangeFeild.text.toString()))
        }

        handleBack()
    }

    override fun render(state: EditAccountInfoState) {
        when (state) {
            is EditAccountInfoState.SetViews -> setViews(state.etValue, state.title)
            is EditAccountInfoState.ShowDialog -> showDialog()
            is EditAccountInfoState.KeepEditing -> dialog.dismiss()
            is EditAccountInfoState.EnableContinue -> enableContinue()
            is EditAccountInfoState.DisableContinue -> disableContinue()
        }
    }

    private fun enableContinue() {
        binding.btnContinue.isEnabled = true
    }

    private fun disableContinue() {
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

    private fun setViews(etValue: String, title: String) {
        dialog = Dialog(requireContext())
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.item_dialog_discard_changes,
            null,
            false
        )
        setWindowColor()
        binding.etChangeFeild.setText(etValue)
        activity?.title = title
        binding.tvToolBar.text = title
    }

    override fun render(effect: EditAccountInfoEffect) {
        when (effect) {
            is EditAccountInfoEffect.NavigateToAccountInfo -> navigateToAccountInfo(effect.editValue)
        }
    }


    //TODO handle with fragment communication
    fun navigateToAccountInfo(editValue : AccountInfoEntity) {
        dialog.dismiss()
        replaceFragment(
            R.id.container,
            requireActivity(),
            AccountInfoV1Fragment.newInstance(editValue),
            AccountInfoV1Fragment::class.java.simpleName,
            true,
            true
        )
    }

    private fun setWindowColor(){
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.background_appbar_color)
        }
    }

    override fun triggerIntent(intent: EditAccountInfoIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }
}