package com.webkul.mobikul.odoo.fragment

import android.util.Log
import androidx.fragment.app.Fragment
import com.webkul.mobikul.odoo.activity.BaseActivity
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.helper.Helper

abstract class BaseFragment : Fragment() {
    override fun onStop() {
        super.onStop()
        AnalyticsImpl.trackFragmentClosed(Helper.getScreenName(title))
        Log.i(TAG, "onStop: ")
        // FIXME: 25/8/17 Changed for Review backpress to work.
        if (this !is ProductReviewFragment) {
            (context as BaseActivity?)?.mCompositeDisposable?.clear()
        }
    }

    abstract var title: String

    open val TAG = "BaseFragment"

    override fun onStart() {
        super.onStart()
        AnalyticsImpl.trackFragmentOpened(Helper.getScreenName(title))
    }
}