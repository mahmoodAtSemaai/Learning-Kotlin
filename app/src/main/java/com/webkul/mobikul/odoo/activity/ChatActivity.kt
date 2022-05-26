package com.webkul.mobikul.odoo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.BundleConstant.*
import com.webkul.mobikul.odoo.databinding.ActivityChatBinding
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.SnackbarHelper


class ChatActivity : BaseActivity() {

    companion object {
        const val TAG = "ChatActivity"
        const val AUTHORIZATION = "Authorization"
        const val LOGIN = "Login"
        const val CHAT_URL_ENDPOINT = "/im_livechat/chat_session?"
        const val SELLER_ID = "seller_id"
        const val USER_ID = "user_id"
    }

    private lateinit var mBinding: ActivityChatBinding

    private var chatUrl: String? = null
    private var uuid: String? = null
    var sellerId: String = ""
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        setSupportActionBar(mBinding.toolbar)
        showBackButton(true)
        userId = AppSharedPref.getCustomerId(this)
        handleIntent(intent)
        setWebView()
        checkChatUrl()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun getScreenTitle(): String {
        return TAG
    }

    private fun handleIntent(intent: Intent) {
        intent.action?.takeIf { action -> action == Intent.ACTION_VIEW }?.let {
            handleNotification(intent)
        } ?: kotlin.run { handleActivityIntent(intent) }
    }

    private fun handleNotification(intent: Intent) {
        chatUrl = intent.data?.getQueryParameter(QUERY_KEY_CHAT_URL)
        uuid = intent.data?.getQueryParameter(QUERY_KEY_CHAT_UUID)
        if (intent.hasExtra(QUERY_KEY_CHAT_TITLE)) {
            val title = intent.getStringExtra(QUERY_KEY_CHAT_TITLE)
            setToolbarTitle(title ?: this.resources.getString(R.string.chat_title))
        } else {
            setToolbarTitle(this.resources.getString(R.string.chat_title))
        }
    }

    private fun handleActivityIntent(intent: Intent) {
        if (intent.hasExtra(BUNDLE_KEY_CHAT_URL)) {
            chatUrl = intent.getStringExtra(BUNDLE_KEY_CHAT_URL)
        }

        if (intent.hasExtra(BUNDLE_KEY_CHAT_UUID)) {
            uuid = intent.getStringExtra(BUNDLE_KEY_CHAT_UUID)
        }

        if (intent.hasExtra(BUNDLE_KEY_SELLER_ID)) {
            sellerId = intent.getStringExtra(BUNDLE_KEY_SELLER_ID) ?: ""
        }

        if (intent.hasExtra(BUNDLE_KEY_CHAT_TITLE)) {
            val title = intent.getStringExtra(BUNDLE_KEY_CHAT_TITLE)
            setToolbarTitle(title ?: this.resources.getString(R.string.chat_title))
        } else {
            setToolbarTitle(this.resources.getString(R.string.chat_title))
        }
    }

    private fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun checkChatUrl() {
        loadUrlWithHeader()
    }

    private fun setWebView() {
        val webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                mBinding.webviewPb.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                mBinding.webviewPb.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
            ) {
                mBinding.webview.stopLoading()
                SnackbarHelper.getSnackbar(
                        this@ChatActivity,
                        getString(R.string.error_something_went_wrong),
                        Snackbar.LENGTH_INDEFINITE,
                        SnackbarHelper.SnackbarType.TYPE_WARNING
                ).show()
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }

        mBinding.webview.settings.apply {
            javaScriptEnabled = true
            allowFileAccess = true
            domStorageEnabled = true
        }

        clearCookies(this)
        mBinding.webview.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        mBinding.webview.isScrollbarFadingEnabled = false
        mBinding.webview.webViewClient = webViewClient

    }

    @SuppressWarnings("deprecation")
    private fun clearCookies(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        } else if (context != null) {
            val cookieSyncManager = CookieSyncManager.createInstance(context)
            cookieSyncManager.startSync()
            val cookieManager: CookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            cookieSyncManager.stopSync()
            cookieSyncManager.sync()
        }
    }

    private fun loadUrlWithHeader() {
        val headerMap = mutableMapOf<String, String>()
        headerMap[AUTHORIZATION] = BuildConfig.BASIC_AUTH_KEY
        headerMap[LOGIN] = AppSharedPref.getCustomerLoginBase64Str(this)
        if (chatUrl == null) {
            chatUrl =
                    "${BuildConfig.BASE_URL}${CHAT_URL_ENDPOINT}$SELLER_ID=$sellerId&$USER_ID=$userId"
        }
        mBinding.webview.loadUrl(chatUrl!!, headerMap)
    }
}

