package com.webkul.mobikul.odoo.domain.usecase.utils

import android.view.ViewGroup
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.ReadMoreEntity
import com.webkul.mobikul.odoo.domain.enums.ReadMoreType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WebViewReadMoreUseCase @Inject constructor(
		private val resourcesProvider : ResourcesProvider
) {
	
	operator fun invoke(params : ViewGroup.LayoutParams) : Flow<Resource<ReadMoreEntity>> = flow {
		emit(Resource.Loading)
		try {
			if (params.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
				val scale : Float = resourcesProvider.getDisplayMetrics().density
				params.height = (30 * scale + 0.5f).toInt()
				emit(Resource.Success(ReadMoreEntity(params,ReadMoreType.READ_MORE.ordinal)))
				
			} else {
				params.height = ViewGroup.LayoutParams.WRAP_CONTENT
				emit(Resource.Success(ReadMoreEntity(params,ReadMoreType.READ_LESS.ordinal)))
			}
		} catch (e : Exception) {
			emit(Resource.Failure(FailureStatus.OTHER, null, e.localizedMessage))
		}
		
	}.flowOn(Dispatchers.IO)
}