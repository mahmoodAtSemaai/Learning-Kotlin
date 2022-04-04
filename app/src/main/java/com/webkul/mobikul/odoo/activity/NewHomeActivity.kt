package com.webkul.mobikul.odoo.activity

import android.os.Bundle
import com.webkul.mobikul.odoo.R

class NewHomeActivity : BaseActivity() {
    private val TAG = this.title.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_home)
    }

    override fun getScreenTitle(): String = TAG
}