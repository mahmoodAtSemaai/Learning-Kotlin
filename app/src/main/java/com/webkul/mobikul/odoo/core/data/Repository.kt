package com.webkul.mobikul.odoo.core.data

import com.webkul.mobikul.odoo.core.utils.Resource

/**
 * Marker Interface for Repository. Every Repository must implement this.
 */
interface Repository<E : Any, K : Any, R : Any> {

	suspend fun create(request: R): Resource<E> = throw Exception("Method not Implemented")
	suspend fun create(): Resource<E> = throw Exception("Method not Implemented")
	suspend fun get(): Resource<E> = throw Exception("Method not Implemented")
	suspend fun get(request: R): Resource<E> = throw Exception("Method not Implemented")
	suspend fun getById(id: K): Resource<E> = throw Exception("Method not Implemented")
	suspend fun getAll(request: R): Resource<List<E>> = throw Exception("Method not Implemented")
	suspend fun update(request: R): Resource<E> = throw Exception("Method not Implemented")
	suspend fun delete(id: K): Resource<E> = throw Exception("Method not Implemented")

}