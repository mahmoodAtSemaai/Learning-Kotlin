package com.webkul.mobikul.odoo.adapter.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemChatListBinding
import com.webkul.mobikul.odoo.handler.chat.ChatHistoryListItemHandler
import com.webkul.mobikul.odoo.model.chat.ChatHistoryResponse

class ChatHistoryListAdapter(
    private val mContext: Context,
    private val chatHistoryList: List<ChatHistoryResponse>
) :
    RecyclerView.Adapter<ChatHistoryListAdapter.ChatHistoryListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ChatHistoryListViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val contactView: View = inflater.inflate(R.layout.item_chat_list, parent, false)
        return ChatHistoryListViewHolder(ItemChatListBinding.bind(contactView))
    }

    override fun onBindViewHolder(holder: ChatHistoryListViewHolder, position: Int) {
        val chatItem = chatHistoryList[position]
        val chatHistoryListItemHandler =
            ChatHistoryListItemHandler(mContext, chatItem, holder.mBinding)
        chatHistoryListItemHandler.setView()
    }

    override fun getItemCount(): Int {
        return chatHistoryList.size
    }

    class ChatHistoryListViewHolder(val mBinding: ItemChatListBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

    }
}