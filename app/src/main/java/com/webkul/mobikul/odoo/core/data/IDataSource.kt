package com.webkul.mobikul.odoo.core.data

import com.webkul.mobikul.odoo.core.utils.Resource

interface IDataSource<M : Any, K : Any, R : Any> {
    suspend fun create(request: R): Resource<M> = throw Exception("Method not Implemented")
    suspend fun create(): Resource<M> = throw Exception("Method not Implemented")
    suspend fun get(): Resource<M> = throw Exception("Method not Implemented")
    suspend fun get(request: R): Resource<M> = throw Exception("Method not Implemented")
    suspend fun getById(id: K): Resource<M> = throw Exception("Method not Implemented")
    suspend fun getAll(request: R): Resource<List<M>> = throw Exception("Method not Implemented")
    suspend fun update(request: R): Resource<M> = throw Exception("Method not Implemented")
    suspend fun delete(id: K): Resource<M> = throw Exception("Method not Implemented")
}