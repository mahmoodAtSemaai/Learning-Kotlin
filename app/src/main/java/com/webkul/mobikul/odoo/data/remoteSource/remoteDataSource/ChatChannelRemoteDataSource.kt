package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.ChatDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ChatServices
import com.webkul.mobikul.odoo.model.chat.ChatCreateChannelResponse
import javax.inject.Inject

class ChatChannelRemoteDataSource @Inject constructor(
        private val apiService: ChatServices,
        gson: Gson,
        appPreferences: AppPreferences
) : ChatDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun create() = safeApiCall(ChatCreateChannelResponse::class.java) {
        apiService.createChatChannel()
    }
}