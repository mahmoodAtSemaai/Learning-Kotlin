package com.webkul.mobikul.odoo.data.entity.address

import com.google.gson.annotations.SerializedName

data class StateListEntity(
    @SerializedName("data") val stateList : List<StateEntity>
)