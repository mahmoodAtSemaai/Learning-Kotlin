package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ChatEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.ChatChannelRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.ChatChannelRepository
import com.webkul.mobikul.odoo.model.chat.ChatCreateChannelResponse
import javax.inject.Inject

class ChatChannelRepositoryImpl @Inject constructor(
    remoteDataSource: ChatChannelRemoteDataSource
) : ChatChannelRepository, BaseRepository<ChatEntity, Any, Any, ChatCreateChannelResponse>() {

    override val entityParser
        get() = ModelEntityParser(ChatEntity::class.java, ChatCreateChannelResponse::class.java)

    override var dataSource: IDataSource<ChatCreateChannelResponse, Any, Any> = remoteDataSource

    override suspend fun create(): Resource<ChatEntity> {
        return super<BaseRepository>.create()
    }
}