package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.CustomerGroupEntity
import com.webkul.mobikul.odoo.domain.repository.CustomerGroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CustomerGroupUseCase @Inject constructor(
    private val customerGroupRepository: CustomerGroupRepository,
    private val resourcesProvider: ResourcesProvider,
    private val appPreferences: AppPreferences
) {
    operator fun invoke(): Flow<Resource<List<CustomerGroupEntity>>> =
        flow {

            emit(Resource.Loading)
            val result = customerGroupRepository.getCustomerGroup()
            when(result) {
                is Resource.Success -> {
                    val comparator = compareBy<CustomerGroupEntity>{it.order}
                    var nameComparator = comparator.thenBy {it.name}
                    if(appPreferences.languageCode == resourcesProvider.getString(R.string.ind_lang)){
                        nameComparator = comparator.thenByDescending {it.name}
                    }
                    val list = result.value.customerGroups.sortedWith(nameComparator)
                    emit(Resource.Success(list))
                }
                is Resource.Failure -> {Resource.Failure(result.failureStatus,result.code,result.message)}
            }

        }.flowOn(Dispatchers.IO)
}