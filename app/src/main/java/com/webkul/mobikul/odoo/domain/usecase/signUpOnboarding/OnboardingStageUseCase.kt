package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.StagesEntity
import com.webkul.mobikul.odoo.domain.repository.OnboardingStageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OnboardingStageUseCase @Inject constructor(
    private val onboardingStageRepository: OnboardingStageRepository
) {
    operator fun invoke(): Flow<Resource<LinkedHashMap<Int,String>>> =
        flow {
            emit(Resource.Loading)
            val result = onboardingStageRepository.getOnboardingStages()
            when(result){
                is Resource.Success -> {
                    val stages = getOnbordingStage(result.value.stageDetails)
                    emit(Resource.Success(stages))
                }
                is Resource.Failure -> {emit(Resource.Failure(result.failureStatus,result.code,result.message))}
                is Resource.Loading -> {emit(Resource.Loading)}
                is Resource.Default -> {emit(Resource.Default)}
            }
        }.flowOn(Dispatchers.IO)

    private fun getOnbordingStage(stageDetails: List<StagesEntity>): LinkedHashMap<Int,String> {
        val updatedStages = LinkedHashMap<Int,String>()
        val comparator = compareBy<StagesEntity>{it.order}
        val sortedStage = stageDetails.sortedWith(comparator)
        for(item in sortedStage){
            updatedStages[item.id] = item.name
        }
        return updatedStages
    }
}