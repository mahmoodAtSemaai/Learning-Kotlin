package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class MobikulCategoryDetailResponse(
        @SerializedName("category") val category: String,
        @SerializedName("category_id") val categoryId: Int,
        @SerializedName("crops") val crops: List<String>,
        @SerializedName("organic") val organic: Boolean,
        @SerializedName("weight") val weight: Float,
        @SerializedName("mrp") val mrp: Float,
        @SerializedName("additional_information") val additionalInformation: String,
        @SerializedName("planting_method") val plantingMethod: String,
        @SerializedName("plant_spacing") val plantSpacing: String,
        @SerializedName("active_ingredients") val activeIngredients: String,
        @SerializedName("dosage") val dosage: String,
        @SerializedName("application_method") val applicationMethod: String,
        @SerializedName("frequency_of_applications") val frequencyOfApplications: String,
        @SerializedName("pests_and_diseases") val pestsAndDiseases: String,
        @SerializedName("duration_of_effect") val durationOfEffect: String,
)