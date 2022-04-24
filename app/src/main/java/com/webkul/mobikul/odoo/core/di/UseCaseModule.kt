package com.webkul.mobikul.odoo.core.di


import com.webkul.mobikul.odoo.features.auth.domain.repo.LoginRepository
import com.webkul.mobikul.odoo.features.auth.domain.usecase.LogInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideLogInUseCase(
            loginRepository: LoginRepository
    ): LogInUseCase = LogInUseCase(loginRepository)


}