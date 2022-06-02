package com.webkul.mobikul.odoo.model.chat

import com.google.gson.annotations.SerializedName

data class ChatCreateChannelResponse(
    @SerializedName("message") val message: String
)
