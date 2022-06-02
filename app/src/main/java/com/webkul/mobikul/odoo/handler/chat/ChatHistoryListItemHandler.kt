package com.webkul.mobikul.odoo.handler.chat

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.core.text.HtmlCompat
import com.webkul.mobikul.odoo.activity.ChatActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.ItemChatListBinding
import com.webkul.mobikul.odoo.model.chat.ChatHistoryResponse

class ChatHistoryListItemHandler(
    private val mContext: Context,
    private val chatListModel: ChatHistoryResponse,
    private val mBinding: ItemChatListBinding
) {

    fun setView() {
        mBinding.data = chatListModel
        if (chatListModel.seen > 0) {
            mBinding.tvRecentChat.setTypeface(null, Typeface.BOLD)
        }
        mBinding.tvRecentChat.text = HtmlCompat.fromHtml(chatListModel.recentChat, HtmlCompat.FROM_HTML_MODE_LEGACY)
        mBinding.chatListItemRootCL.setOnClickListener {
            launchChatWebViewActivity()
        }
    }

    private fun launchChatWebViewActivity() {
        Intent(mContext, ChatActivity::class.java).apply {
            putExtra(BundleConstant.BUNDLE_KEY_CHAT_URL, chatListModel.chatUrl)
            putExtra(BundleConstant.BUNDLE_KEY_CHAT_UUID, chatListModel.chatUuid)
            putExtra(BundleConstant.BUNDLE_KEY_CHAT_TITLE, chatListModel.title)
            mContext.startActivity(this)
        }
    }

}