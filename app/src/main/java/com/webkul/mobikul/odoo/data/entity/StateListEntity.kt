package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class StateListEntity(
    val status: String?,

    @SerializedName("data")
    val states: ArrayList<StateEntity>,

    )
