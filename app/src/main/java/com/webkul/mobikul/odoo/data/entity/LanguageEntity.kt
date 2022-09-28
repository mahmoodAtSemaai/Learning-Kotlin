package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class LanguageEntity(
        @SerializedName("language_code") val languageCode: String,
        @SerializedName("language") val language: String
)
