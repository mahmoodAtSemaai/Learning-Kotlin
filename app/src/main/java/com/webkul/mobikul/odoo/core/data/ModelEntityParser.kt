package com.webkul.mobikul.odoo.core.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ModelEntityParser<E : Any, M : Any>(
        private var entityType: Class<E>,
        private var modelType: Class<M>,
) {

    private val gson = Gson()

    fun toObject(value: M): E {
        return gson.fromJson(gson.toJson(value), entityType)
    }

    fun toListObject(value: List<M>): List<E> {
        val listToken: Type = object : TypeToken<ArrayList<M>>() {}.type
        return gson.fromJson(gson.toJson(value), listToken)
    }

    fun fromObject(value: E): M {
        return gson.fromJson(gson.toJson(value), modelType)
    }

    fun fromListObject(value: List<E>): List<M> {
        val listToken: Type = object : TypeToken<ArrayList<M>>() {}.type
        return gson.fromJson(gson.toJson(value), listToken)
    }

}