package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ChatEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.ChatChannelRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.ChatChannelRepository
import javax.inject.Inject

class ChatChannelRepositoryImpl @Inject constructor(
    private val remoteDataSource: ChatChannelRemoteDataSource
) : ChatChannelRepository {

    override suspend fun createChatChanel(): Resource<ChatEntity> {
        return remoteDataSource.createChatChannel()
    }
}