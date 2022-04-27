package com.webkul.mobikul.odoo.core.platform

import androidx.fragment.app.Fragment
import com.webkul.mobikul.odoo.constant.PageConstants.Companion.DEFAULT_PAGE

abstract class BaseFragment : Fragment() {
    abstract val layoutId: Int

    open fun setupViews(){}

    open fun getPageName():String{
        return DEFAULT_PAGE
    }
}