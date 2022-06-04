package com.webkul.mobikul.odoo.core.di


import android.content.Context
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import com.webkul.mobikul.odoo.domain.repository.AuthRepository
import com.webkul.mobikul.odoo.domain.repository.TermsConditionRepository
import com.webkul.mobikul.odoo.domain.usecase.auth.*
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
            authRepository: AuthRepository
    ): LogInUseCase = LogInUseCase(authRepository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(
            authRepository: AuthRepository,
            @ApplicationContext context: Context
    ): SignUpUseCase = SignUpUseCase(authRepository, context)

    @Provides
    @Singleton
    fun provideViewPrivacyPolicyUseCase(@ApplicationContext context: Context): ViewPrivacyPolicyUseCase =
            ViewPrivacyPolicyUseCase(context)

    @Provides
    @Singleton
    fun provideBillingAddressUseCase(
            addressRepository: AddressRepository
    ): BillingAddressUseCase = BillingAddressUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideCountryStateUseCase(
            addressRepository: AddressRepository
    ): CountryStateUseCase = CountryStateUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideViewMarketPlaceTnCUseCase(
            termsConditionRepository: TermsConditionRepository
    ): ViewMarketPlaceTnCUseCase = ViewMarketPlaceTnCUseCase(termsConditionRepository)

    @Provides
    @Singleton
    fun provideViewTnCUseCase(
            termsConditionRepository: TermsConditionRepository
    ): ViewTnCUseCase = ViewTnCUseCase(termsConditionRepository)

}