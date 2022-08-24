package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import javax.inject.Inject

class NextOnboardingStageUseCase @Inject constructor(private val resourcesProvider: ResourcesProvider) {
    operator fun invoke(incompleteStages : LinkedHashMap<Int,String>) : String {
        if(incompleteStages.isEmpty()){
            return resourcesProvider.getString(R.string.home_page_stage)
        }else {
            val (key, value) = incompleteStages.entries.iterator().next()
            return value
        }
    }
}