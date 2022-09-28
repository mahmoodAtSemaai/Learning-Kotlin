package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class LanguageResponse(
        @SerializedName("language_code") val languageCode: String,
        @SerializedName("language") val language: String
)
