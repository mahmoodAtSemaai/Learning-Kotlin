package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.ChatEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ChatServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.HomeServicesV1
import javax.inject.Inject

class ChatChannelRemoteDataSource @Inject constructor(
    private val apiService: ChatServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {

    suspend fun createChatChannel() = safeApiCall(ChatEntity::class.java) {
        apiService.createChatChannel()
    }
}