package com.webkul.mobikul.odoo.handler.chat

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.text.HtmlCompat
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.ChatActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.ItemChatListBinding
import com.webkul.mobikul.odoo.model.chat.ChatBaseResponse
import com.webkul.mobikul.odoo.model.chat.ChatHistoryResponse

class ChatHistoryListItemHandler(
        private val mContext: Context,
        private val chatListModel: ChatHistoryResponse,
        private val binding: ItemChatListBinding
) {

    fun setView() {
        binding.data = chatListModel
        setUnreadChatCount(chatListModel.seen)
        binding.tvRecentChat.text = HtmlCompat.fromHtml(chatListModel.recentChat, HtmlCompat.FROM_HTML_MODE_LEGACY).trim().toString()
        binding.chatListItemRootCL.setOnClickListener {
            launchChatWebViewActivity()
        }
    }

    private fun setUnreadChatCount(chatUnreadMessageCount: Int) {
        if (chatUnreadMessageCount > 0) {
            binding.tvName.setTextColor(mContext.resources.getColor(R.color.black))
            binding.tvRecentChat.setTextColor(mContext.resources.getColor(R.color.black))
            binding.ivUnreadChatCount.visibility = View.VISIBLE
            binding.ivUnreadChatCount.text =
                    if (chatUnreadMessageCount > 9) binding.ivUnreadChatCount.context.getString(R.string.text_nine_plus)
                    else chatUnreadMessageCount.toString()
            binding.ivUnreadChatCount.visibility = View.VISIBLE
        } else {
            binding.ivUnreadChatCount.visibility = View.INVISIBLE
        }
    }

    private fun launchChatWebViewActivity() {
        Intent(mContext, ChatActivity::class.java).apply {
            putExtra(BundleConstant.BUNDLE_KEY_CHAT_URL, chatListModel.chatUrl)
            putExtra(BundleConstant.BUNDLE_KEY_CHAT_UUID, chatListModel.chatUuid)
            putExtra(BundleConstant.BUNDLE_KEY_CHAT_TITLE, chatListModel.title)
            putExtra(BundleConstant.BUNDLE_KEY_CHAT_PROFILE_PICTURE_URL, chatListModel.imageUrl)
            mContext.startActivity(this)
        }
    }

}