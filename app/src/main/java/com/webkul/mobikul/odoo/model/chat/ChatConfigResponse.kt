package com.webkul.mobikul.odoo.model.chat

import com.google.gson.annotations.SerializedName

data class ChatConfigResponse(
    @SerializedName("chat_url") val chatUrl: String,
    @SerializedName("channel_name") val channelName: String
)
