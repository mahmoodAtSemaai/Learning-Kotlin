package com.webkul.mobikul.odoo.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.adapter.chat.ChatHistoryListAdapter
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.ActivityChatHistoryBinding
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.model.chat.ChatBaseResponse
import com.webkul.mobikul.odoo.model.chat.ChatHistoryResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ChatHistoryActivity : BaseActivity() {

    companion object {
        val TAG = "ChatHistoryActivity"
    }

    private lateinit var mBinding: ActivityChatHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat_history)
        setSupportActionBar(mBinding.toolbar)
        setToolbarTitle(this.resources.getString(R.string.chat_title))
        showBackButton(true)
    }

    override fun getScreenTitle(): String {
        return TAG
    }

    private fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onResume() {
        super.onResume()
        isUserLoggedIn()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun isUserLoggedIn() {
        if (AppSharedPref.isLoggedIn(this)) {
            getChatHistory()
        } else {
            showLoginDialog()
        }
    }

    private fun showLoginDialog() {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(R.drawable.ic_warning)
                .setTitleText(getString(R.string.error))
                .setContentText(getString(R.string.error_please_login_to_continue))
                .setConfirmText(getString(R.string.login))
                .setConfirmClickListener { sweetAlertDialog: SweetAlertDialog ->
                    AppSharedPref.clearCustomerData(this)
                    val intent = Intent(this, SignInSignUpActivity::class.java)
                    intent.putExtra(
                            BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY,
                            ChatHistoryActivity::class.java.simpleName
                    )
                    intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    sweetAlertDialog.dismiss()
                }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun getChatHistory() {
        ApiConnection.getChatHistory(this).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(object :
                        CustomObserver<ChatBaseResponse<List<ChatHistoryResponse>>>(this) {
                    override fun onNext(chatHistoryResponse: ChatBaseResponse<List<ChatHistoryResponse>>) {
                        super.onNext(chatHistoryResponse)
                        chatHistoryResponse.data?.let { setView(it) }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                    }
                })
    }

    private fun setView(chatHistory: List<ChatHistoryResponse>) {
        if (chatHistory.isEmpty()) {
            mBinding.chatList.visibility = View.GONE
            mBinding.tvNochats.visibility = View.VISIBLE
            mBinding.noChatIv.visibility = View.VISIBLE
        } else {
            mBinding.tvNochats.visibility = View.GONE
            mBinding.noChatIv.visibility = View.GONE
            mBinding.chatList.apply {
                layoutManager = LinearLayoutManager(this@ChatHistoryActivity)
                adapter = ChatHistoryListAdapter(this@ChatHistoryActivity, chatHistory)

            }
            mBinding.chatList.visibility = View.VISIBLE
        }
    }

}