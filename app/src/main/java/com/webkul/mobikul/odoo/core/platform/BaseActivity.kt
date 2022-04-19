package com.webkul.mobikul.odoo.core.platform

import androidx.appcompat.app.AppCompatActivity
import com.webkul.mobikul.odoo.constant.PageConstants.Companion.DEFAULT_PAGE

abstract class BaseActivity : AppCompatActivity() {
    abstract val contentViewId: Int

    open fun setupViews(){}

    open fun getPageName():String{
        return DEFAULT_PAGE
    }

}