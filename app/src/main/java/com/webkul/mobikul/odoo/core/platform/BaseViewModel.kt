package com.webkul.mobikul.odoo.core.platform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.webkul.mobikul.odoo.core.exception.Failure

/**
 * Base ViewModel class with default com.webkul.mobikul.odoo.core.exception.Failure handling.
 * @see ViewModel
 * @see com.webkul.mobikul.odoo.core.exception.Failure
 */
abstract class BaseViewModel : ViewModel() {

    private val _failure: MutableLiveData<Failure> = MutableLiveData()
    val failure: LiveData<Failure> = _failure

    protected fun handleFailure(failure: Failure) {
        _failure.value = failure
    }
}