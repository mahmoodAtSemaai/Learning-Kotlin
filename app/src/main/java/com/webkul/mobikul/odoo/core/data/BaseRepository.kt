package com.webkul.mobikul.odoo.core.data

import com.webkul.mobikul.odoo.core.utils.Resource

abstract class BaseRepository<E : Any, K : Any, R : Any, M : Any> : Repository<E, K, R> {

    abstract val entityParser: ModelEntityParser<E, M>
    abstract var dataSource: IDataSource<M, K, R>

    override suspend fun create(request: R): Resource<E> {
        return when (val result = dataSource.create(request)) {
            is Resource.Success -> {
                val returnObject = entityParser.toObject(result.value)
                Resource.Success(returnObject)
            }
            is Resource.Default -> Resource.Default
            is Resource.Failure -> Resource.Failure(
                result.failureStatus,
                result.code,
                result.message
            )
            is Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun create(): Resource<E> {
        return when (val result = dataSource.create()) {
            is Resource.Default -> Resource.Default
            is Resource.Failure -> Resource.Failure(
                result.failureStatus,
                result.code,
                result.message
            )
            is Resource.Loading -> Resource.Loading
            is Resource.Success -> Resource.Success(entityParser.toObject(result.value))
        }
    }

    override suspend fun get(): Resource<E> {
        return when (val result = dataSource.get()) {
            is Resource.Success -> {
                val returnObject = entityParser.toObject(result.value)
                Resource.Success(returnObject)
            }
            is Resource.Default -> Resource.Default
            is Resource.Failure -> Resource.Failure(
                result.failureStatus,
                result.code,
                result.message
            )
            is Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun getById(id: K): Resource<E> {
        return when (val result = dataSource.getById(id)) {
            is Resource.Success -> {
                val returnObject = entityParser.toObject(result.value)
                Resource.Success(returnObject)
            }
            is Resource.Default -> Resource.Default
            is Resource.Failure -> Resource.Failure(
                result.failureStatus,
                result.code,
                result.message
            )
            is Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun get(request: R): Resource<E> {
        return when (val result = dataSource.get(request)) {
            is Resource.Success -> {
                val returnObject = entityParser.toObject(result.value)
                Resource.Success(returnObject)
            }
            is Resource.Default -> Resource.Default
            is Resource.Failure -> Resource.Failure(
                result.failureStatus,
                result.code,
                result.message
            )
            is Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun getAll(request: R): Resource<List<E>> {
        return when (val result = dataSource.getAll(request)) {
            is Resource.Success -> {
                val returnObject = entityParser.toListObject(result.value)
                Resource.Success(returnObject)
            }
            is Resource.Default -> Resource.Default
            is Resource.Failure -> Resource.Failure(
                result.failureStatus,
                result.code,
                result.message
            )
            is Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun update(request: R): Resource<E> {
        return when (val result = dataSource.update(request)) {
            is Resource.Success -> {
                val returnObject = entityParser.toObject(result.value)
                Resource.Success(returnObject)
            }
            is Resource.Default -> Resource.Default
            is Resource.Failure -> Resource.Failure(
                result.failureStatus,
                result.code,
                result.message
            )
            is Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun delete(id: K): Resource<E> {
        return when (val result = dataSource.delete(id)) {
            is Resource.Success -> {
                val returnObject = entityParser.toObject(result.value)
                Resource.Success(returnObject)
            }
            is Resource.Default -> Resource.Default
            is Resource.Failure -> Resource.Failure(
                result.failureStatus,
                result.code,
                result.message
            )
            is Resource.Loading -> Resource.Loading
        }
    }
}