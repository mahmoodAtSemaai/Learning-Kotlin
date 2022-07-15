package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.chat.ChatBaseResponse
import com.webkul.mobikul.odoo.model.chat.ChatCreateChannelResponse
import retrofit2.http.POST

interface ChatServices {

    @POST(ApiInterface.CHAT_CREATE_CHANNEL)
    suspend fun createChatChannel(): ChatBaseResponse<ChatCreateChannelResponse>
}