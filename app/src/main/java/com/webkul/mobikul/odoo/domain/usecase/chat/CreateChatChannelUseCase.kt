package com.webkul.mobikul.odoo.domain.usecase.chat

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ChatEntity
import com.webkul.mobikul.odoo.domain.repository.ChatChannelRepository
import com.webkul.mobikul.odoo.updates.FirebaseRemoteConfigHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CreateChatChannelUseCase @Inject constructor(
        private val chatChannelRepository: ChatChannelRepository,
        private val appPreferences: AppPreferences
) {
    operator fun invoke(): Flow<Resource<ChatEntity>> = flow {

        emit(Resource.Loading)
        if (appPreferences.isSeller && FirebaseRemoteConfigHelper.isChatFeatureEnabled) {
            val result = chatChannelRepository.createChatChanel()
            emit(result)
        } else {
            emit(Resource.Default)
        }

    }.flowOn(Dispatchers.IO)

}