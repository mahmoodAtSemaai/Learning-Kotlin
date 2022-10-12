package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDetailsViewsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val resourcesProvider: ResourcesProvider
) {
    suspend operator fun invoke(): Pair<Boolean, List<String>> {
        when (val user = userRepository.get()) {
            is Resource.Success -> {
                val viewDetails = mutableListOf(user.value.groupName)
                var hideGroupName = true
                //TODO optimize this
                when (user.value.groupName) {
                    resourcesProvider.getString(R.string.toko_tani) -> {
                        hideGroupName = false
                        viewDetails.add(resourcesProvider.getString(R.string.thank_you_for_joining_semaai_toko_tani))
                        viewDetails.add(resourcesProvider.getString(R.string.toko_tani_name))
                        viewDetails.add(resourcesProvider.getString(R.string.toko_tani_photo))
                    }
                    resourcesProvider.getString(R.string.kelompok_tani) -> {
                        hideGroupName = false
                        viewDetails.add(resourcesProvider.getString(R.string.thank_you_for_joining_semaai_kelompok_tani))
                        viewDetails.add(resourcesProvider.getString(R.string.kelompok_tani_name))
                        viewDetails.add(resourcesProvider.getString(R.string.kelompok_tani_photo))
                    }
                    else -> {
                        hideGroupName = true
                        viewDetails.add(resourcesProvider.getString(R.string.thank_you_for_joining_semaai_farmers_others))
                        viewDetails.add(resourcesProvider.getString(R.string.kelompok_tani_name))
                        viewDetails.add(resourcesProvider.getString(R.string.farmers_others_photo))
                    }
                }
                return Pair(hideGroupName, viewDetails)
            }

            else -> return Pair(true, emptyList())
        }

    }
}