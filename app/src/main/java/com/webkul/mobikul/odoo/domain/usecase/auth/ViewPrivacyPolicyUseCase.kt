package com.webkul.mobikul.odoo.domain.usecase.auth

import android.content.Context
import android.content.Intent
import com.webkul.mobikul.odoo.core.extension.onPrivacyPolicyClick
import com.webkul.mobikul.odoo.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ViewPrivacyPolicyUseCase @Inject constructor(private val context: Context) {

    operator fun invoke(): Flow<Resource<Intent>> = flow {
        emit(Resource.Loading)
        val result = context.onPrivacyPolicyClick()
        emit(result)

    }.flowOn(Dispatchers.IO)
}