package com.webkul.mobikul.odoo.features.auth.presentation


import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.domain.usecase.LogInUseCase
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val appPreferences: AppPreferences
) : BaseViewModel() {

    private val _logInResponse = MutableStateFlow<Resource<LoginResponse>>(Resource.Default)
    val logInResponse = _logInResponse

    fun onLogInClicked(username:String , password:String) {
        logInUseCase.invoke()
            .catch { exception ->  }
            .onEach { result ->
                _logInResponse.value = result
            }
            .launchIn(viewModelScope)
    }

    fun onForgotPasswordClicked() {

    }
}