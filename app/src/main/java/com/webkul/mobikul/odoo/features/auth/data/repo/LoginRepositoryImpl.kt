package com.webkul.mobikul.odoo.features.auth.data.repo

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.data.remoteSource.LoginRemoteDataSource
import com.webkul.mobikul.odoo.features.auth.domain.repo.LoginRepository
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
  private val remoteDataSource: LoginRemoteDataSource,
  private val appPreferences: AppPreferences
) : LoginRepository {

  override
  suspend fun logIn(username: String, password: String): Resource<LoginResponse> {
    return remoteDataSource.logIn()
  }
}