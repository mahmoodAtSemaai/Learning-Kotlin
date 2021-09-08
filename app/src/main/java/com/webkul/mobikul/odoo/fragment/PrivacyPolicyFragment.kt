package com.webkul.mobikul.odoo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.Helper

class PrivacyPolicyFragment : Fragment(R.layout.fragment_privacy_policy) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_privacy_policy, container, false)

        val myWebView = view.findViewById<WebView>(R.id.privacy_policy)
        myWebView.loadUrl(AppSharedPref.getPrivacyURL(context))

        Helper.enableDarkModeInWebView(context, myWebView)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): PrivacyPolicyFragment {
            return PrivacyPolicyFragment()
        }
    }
}