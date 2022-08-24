package com.webkul.mobikul.odoo.ui.account.viewModel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.AccountInfoEntity
import com.webkul.mobikul.odoo.ui.account.effect.EditAccountInfoEffect
import com.webkul.mobikul.odoo.ui.account.intent.EditAccountInfoIntent
import com.webkul.mobikul.odoo.ui.account.state.EditAccountInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditAccountInfoViewModel @Inject constructor(private val resourcesProvider: ResourcesProvider) :
    BaseViewModel(),
    IModel<EditAccountInfoState, EditAccountInfoIntent, EditAccountInfoEffect> {
    override val intents: Channel<EditAccountInfoIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<EditAccountInfoState>(EditAccountInfoState.Idle)
    override val state: StateFlow<EditAccountInfoState>
        get() = _state

    private val _effect = Channel<EditAccountInfoEffect>()
    override val effect: Flow<EditAccountInfoEffect>
        get() = _effect.receiveAsFlow()

    var etValue = ""
    var toolbarTitle = ""
    var isName = false
    var accountInfoData = AccountInfoEntity()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is EditAccountInfoIntent.GetArgs -> getArgs(it.arguments)
                    is EditAccountInfoIntent.Discard -> navigateToAccountInfo()
                    is EditAccountInfoIntent.KeepEditing -> keepEditing()
                    is EditAccountInfoIntent.EditValue -> editValueListner(it.value)
                    is EditAccountInfoIntent.BackNavigation -> backNavigation(it.value)
                    is EditAccountInfoIntent.Save -> saveInfo(it.value)
                }
            }
        }
    }

    private suspend fun saveInfo(value: String) {
        if (isName) {
            if(accountInfoData.groupName == resourcesProvider.getString(R.string.farmers) || accountInfoData.groupName == resourcesProvider.getString(R.string.others))
                accountInfoData.name = value
            else
                accountInfoData.userName = value
        } else {
            accountInfoData.customerGroupName = value
        }
        accountInfoData.isEdited = true
        _effect.send(EditAccountInfoEffect.NavigateToAccountInfo(accountInfoData))
    }

    private suspend fun backNavigation(value: String) {
        if (value.equals(etValue)) {
            _effect.send(EditAccountInfoEffect.NavigateToAccountInfo(accountInfoData))
        } else {
            _state.value = EditAccountInfoState.ShowDialog
        }
    }

    private fun editValueListner(value: String) {
        if (value.equals(etValue)) {
            _state.value = EditAccountInfoState.DisableContinue
        } else {
            _state.value = EditAccountInfoState.EnableContinue
        }
    }

    private fun keepEditing() {
        _state.value = EditAccountInfoState.KeepEditing
    }

    private suspend fun navigateToAccountInfo() {
        _effect.send(EditAccountInfoEffect.NavigateToAccountInfo(accountInfoData))
    }

    private fun getArgs(arguments: Bundle?) {
        accountInfoData =
            arguments?.getParcelable(BundleConstant.BUNDLE_KEY_ACCOUNT_INFO_DATA) ?: accountInfoData
        toolbarTitle = arguments?.getString(BundleConstant.BUNDLE_KEY_TITLE) ?: ""
        isName =
            arguments?.getBoolean(BundleConstant.BUNDLE_KEY_IS_USER_NAME_EDIT) ?: false
        if (isName) {
            //TODO optimize
            if (accountInfoData.groupName == resourcesProvider.getString(R.string.farmers) || accountInfoData.groupName == resourcesProvider.getString(R.string.others))
                etValue = accountInfoData.name
            else
                etValue = accountInfoData.userName
        } else {
            etValue = accountInfoData.customerGroupName
        }
        _state.value = EditAccountInfoState.SetViews(etValue, toolbarTitle)
    }
}