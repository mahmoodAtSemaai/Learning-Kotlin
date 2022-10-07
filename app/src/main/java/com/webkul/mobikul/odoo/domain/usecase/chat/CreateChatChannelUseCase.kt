package com.webkul.mobikul.odoo.domain.usecase.chat

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ChatEntity
import com.webkul.mobikul.odoo.data.entity.UserEntity
import com.webkul.mobikul.odoo.domain.repository.ChatChannelRepository
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import com.webkul.mobikul.odoo.updates.FirebaseRemoteConfigHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CreateChatChannelUseCase @Inject constructor(
        private val chatChannelRepository: ChatChannelRepository
) {
    operator fun invoke(userEntity: UserEntity?): Flow<Resource<ChatEntity>> = flow {
        emit(Resource.Loading)
        val isSeller = userEntity?.isSeller ?: false
        if (isSeller && FirebaseRemoteConfigHelper.isChatFeatureEnabled) {
            val result = chatChannelRepository.create()
            emit(result)
        } else {
            emit(Resource.Default)
        }

    }.flowOn(Dispatchers.IO)

}