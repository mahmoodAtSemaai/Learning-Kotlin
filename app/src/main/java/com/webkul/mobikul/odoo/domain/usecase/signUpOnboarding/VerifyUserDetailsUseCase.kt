package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerifyUserDetailsUseCase @Inject constructor(
    private val appPreferences: AppPreferences,
    private val resourcesProvider: ResourcesProvider
) {
    operator fun invoke(name: String, groupName: String): Flow<Boolean> =
        flow {
            val customerGroupName = appPreferences.groupName
            if ((customerGroupName == resourcesProvider.getString(R.string.toko_tani)) || (customerGroupName == resourcesProvider.getString(
                    R.string.kelompok_tani))) {
                emit(name.isNotEmpty() && groupName.isNotEmpty())
            } else {
                emit(name.isNotEmpty())
            }
        }.flowOn(Dispatchers.IO)
}