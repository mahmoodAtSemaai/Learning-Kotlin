package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.utils.Resource

interface BagItemsCountRepository {
	
	suspend fun get() : Resource<Int>
	
}