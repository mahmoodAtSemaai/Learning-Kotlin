package com.webkul.mobikul.odoo.core.di


import android.content.Context
import com.webkul.mobikul.odoo.features.auth.domain.repo.LoginRepository
import com.webkul.mobikul.odoo.features.auth.domain.repo.SignUpRepository
import com.webkul.mobikul.odoo.features.auth.domain.usecase.LogInUseCase
import com.webkul.mobikul.odoo.features.auth.domain.usecase.SignUpUseCase
import com.webkul.mobikul.odoo.features.auth.domain.usecase.ViewPrivacyPolicyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideSignUpUseCase(
        signupRepository: SignUpRepository,
        @ApplicationContext context: Context
    ): SignUpUseCase = SignUpUseCase(signupRepository , context)


    @Provides
    @Singleton
    fun provideViewPrivacyPolicyUseCase(@ApplicationContext context: Context): ViewPrivacyPolicyUseCase =
        ViewPrivacyPolicyUseCase(context)


}