package com.webkul.mobikul.odoo.model.chat

import com.google.gson.annotations.SerializedName

data class ChatHistoryResponse(
    @SerializedName("title") val title: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("time_stamp") val timeStamp: String,
    @SerializedName("recent_chat") val recentChat: String,
    @SerializedName("chat_url") val chatUrl: String,
    @SerializedName("chat_uuid") val chatUuid: String,
    @SerializedName("seen") val seen: Int,
)
