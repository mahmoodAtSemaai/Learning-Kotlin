package com.webkul.mobikul.odoo.core.platform

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BindingBaseActivity<T : ViewDataBinding?>: BaseActivity() {
    protected var _binding: T? = null
    open val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, contentViewId)
        _binding?.lifecycleOwner = this
    }
}