package com.webkul.mobikul.odoo.model.chat

import com.google.gson.annotations.SerializedName

data class ChatBaseResponse<T>(
    @SerializedName("status") val status: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("unread_messages_count") val unreadMessagesCount: Int,
    @SerializedName("data") val data: T?
)
