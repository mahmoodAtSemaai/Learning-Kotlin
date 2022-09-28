package com.webkul.mobikul.odoo.domain.usecase.product

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.domain.UseCase
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.model.generic.ProductData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConvertProductDetailsUseCase @Inject constructor(
    private val resourcesProvider: ResourcesProvider
) : UseCase {

    operator fun invoke(productData: ProductEntity): Flow<Resource<MutableMap<String, List<String>>>> =
        flow {

            emit(Resource.Loading)

            val details = mutableMapOf<String, List<String>>()
            val mobikulCategoryDetails = productData.mobikulCategoryDetails
            details[resourcesProvider.getString(R.string.product_details_category)] =
                mutableListOf(mobikulCategoryDetails!!.category.toString())
            details[resourcesProvider.getString(R.string.product_details_brand)] =
                mutableListOf(productData.brandName!!)
            details[resourcesProvider.getString(R.string.product_details_active_ingredients)] =
                mutableListOf(mobikulCategoryDetails.activeIngredients.toString())
            details[resourcesProvider.getString(R.string.product_details_dosage)] =
                mutableListOf(mobikulCategoryDetails!!.dosage.toString())
            val crops: MutableList<String> = mutableListOf<String>()
            for (crop in mobikulCategoryDetails.crops) {
                crops.add(crop)
            }
            details[resourcesProvider.getString(R.string.product_details_crops)] = crops
            if (mobikulCategoryDetails.organic)
                details[resourcesProvider.getString(R.string.product_details_organic)] =
                    mutableListOf(resourcesProvider.getString(R.string.yes))
            else
                details[resourcesProvider.getString(R.string.product_details_organic)] =
                    mutableListOf(resourcesProvider.getString(R.string.no))
            details[resourcesProvider.getString(R.string.product_details_weight)] =
                mutableListOf(mobikulCategoryDetails.weight.toString())
            details[resourcesProvider.getString(R.string.product_details_mrp)] =
                mutableListOf(mobikulCategoryDetails.mrp.toString())
            details[resourcesProvider.getString(R.string.product_details_planting_method)] =
                mutableListOf(mobikulCategoryDetails.plantingMethod.toString())
            details[resourcesProvider.getString(R.string.product_details_duration_of_effect)] =
                mutableListOf(mobikulCategoryDetails.durationOfEffect.toString())
            details[resourcesProvider.getString(R.string.product_details_planting_spacing)] =
                mutableListOf(mobikulCategoryDetails.plantSpacing.toString())
            details[resourcesProvider.getString(R.string.product_details_pests_and_diseases)] =
                mutableListOf(mobikulCategoryDetails.pestsAndDiseases.toString())
            details[resourcesProvider.getString(R.string.product_details_application_method)] =
                mutableListOf(mobikulCategoryDetails.applicationMethod.toString())
            details[resourcesProvider.getString(R.string.product_details_frequency_of_application)] =
                mutableListOf(mobikulCategoryDetails.frequencyOfApplications.toString())

            val detailsUpdated = mutableMapOf<String, List<String>>()
            for (item in details.entries.iterator()) {
                val checkEmptyList =
                    ((item.value.toString() == "[${resourcesProvider.getString(R.string.product_details_weight_zero)}]") or (item.value.toString() == "[]"))
                if (!checkEmptyList) {
                    detailsUpdated[item.key] = item.value
                }
            }

            emit(Resource.Success(detailsUpdated))
        }

}