package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import javax.inject.Inject

class IncompleteStagesUseCase @Inject constructor() {
    operator fun invoke(
        completedStageId: List<Int>,
        allStageId: LinkedHashMap<Int, String>
    ): LinkedHashMap<Int, String> {
        for(item in completedStageId){
            if(allStageId.containsKey(item)){
                allStageId.remove(item)     //removes the completed stage id from allStage id
            }
        }
        return allStageId
    }
}