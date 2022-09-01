package com.webkul.mobikul.odoo.fragment

import androidx.fragment.app.Fragment
import com.webkul.mobikul.odoo.activity.BaseActivity
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.helper.Helper

abstract class BaseFragment : Fragment() {
    override fun onStop() {
        super.onStop()
        trackAnalyticsEvent(AnalyticsEvent.FRAGMENT_CLOSED)
        // FIXME: 25/8/17 Changed for Review backpress to work.
        if(context is com.webkul.mobikul.odoo.core.platform.BaseActivity){
                //TODO fix this back stack crash issue
            (context as com.webkul.mobikul.odoo.core.platform.BaseActivity).finish()
            return
        }
        if (this !is ProductReviewFragment) {
            (context as BaseActivity?)?.mCompositeDisposable?.clear()
        }
    }

    abstract var title: String

    open val TAG = "BaseFragment"

    override fun onStart() {
        super.onStart()
        trackAnalyticsEvent(AnalyticsEvent.FRAGMENT_OPENED)
    }

    private fun trackAnalyticsEvent(event: AnalyticsEvent) {
        if(this !is EmptyFragment) {
            when(event) {
                AnalyticsEvent.FRAGMENT_CLOSED -> {
                    AnalyticsImpl.trackFragmentClosed(Helper.getScreenName(title))
                }
                AnalyticsEvent.FRAGMENT_OPENED -> {
                    AnalyticsImpl.trackFragmentOpened(Helper.getScreenName(title))
                }
            }
        }
    }

    private enum class AnalyticsEvent {
        FRAGMENT_OPENED,
        FRAGMENT_CLOSED
    }
}