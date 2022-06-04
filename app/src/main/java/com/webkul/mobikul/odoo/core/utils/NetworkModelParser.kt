package com.webkul.mobikul.odoo.core.utils

import com.google.gson.Gson

class NetworkModelParser<E , D>(private var domainType: Class<D>, private var entityType: Class<E>) {

    private val gson = Gson()

    fun toObject(value: E): D {
        val value = gson.fromJson(gson.toJson(value), domainType)
        return value
    }
}