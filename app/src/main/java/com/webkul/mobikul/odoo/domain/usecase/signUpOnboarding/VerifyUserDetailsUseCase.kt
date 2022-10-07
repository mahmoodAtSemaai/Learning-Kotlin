package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerifyUserDetailsUseCase @Inject constructor(
	private val userRepository: UserRepository,
	private val resourcesProvider: ResourcesProvider
) {
	operator fun invoke(name: String, groupName: String): Flow<Boolean> =
		flow {
			when (val user = userRepository.get()) {
				is Resource.Success -> {
					if ((user.value.groupName == resourcesProvider.getString(R.string.toko_tani)) || (user.value.groupName == resourcesProvider.getString(
							R.string.kelompok_tani
						))
					) {
						emit(name.isNotEmpty() && groupName.isNotEmpty())
					} else {
						emit(name.isNotEmpty())
					}
				}
				else -> {
					emit(name.isNotEmpty())
				}
			}
		}.flowOn(Dispatchers.IO)
}