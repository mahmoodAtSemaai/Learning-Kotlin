package com.webkul.mobikul.odoo.model.chat

import com.google.gson.annotations.SerializedName

data class ChatUnreadMessageCount(
    @SerializedName("unread_messages_count") val unreadMessagesCount: Int
)
