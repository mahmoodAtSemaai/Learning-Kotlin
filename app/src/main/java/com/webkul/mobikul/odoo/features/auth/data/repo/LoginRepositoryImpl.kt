package com.webkul.mobikul.odoo.features.auth.data.repo

import com.webkul.mobikul.odoo.features.auth.data.remoteSource.LoginRemoteDataSource
import com.webkul.mobikul.odoo.features.auth.domain.repo.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
  private val remoteDataSource: LoginRemoteDataSource
) : LoginRepository {

  override
  suspend fun logIn() = remoteDataSource.logIn()
}