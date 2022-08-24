package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.*
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.AuthenticationServices
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.HomePageServices
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.SplashServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkServiceModule {

    @Provides
    @Singleton
    fun provideAuthServices(retrofit: Retrofit): AuthServices =
            retrofit.create(AuthServices::class.java)

    @Provides
    @Singleton
    fun provideAddressServices(retrofit: Retrofit): AddressServices =
            retrofit.create(AddressServices::class.java)

    @Provides
    @Singleton
    fun provideCountryStateServices(retrofit: Retrofit): CountryStateServices =
            retrofit.create(CountryStateServices::class.java)

    @Provides
    @Singleton
    fun provideTermsConditionServices(retrofit: Retrofit): TermsConditionServices =
            retrofit.create(TermsConditionServices::class.java)

    @Provides
    @Singleton
    fun provideAuthenticationServices(retrofit: Retrofit): AuthenticationServices =
            retrofit.create(AuthenticationServices::class.java)

    @Provides
    @Singleton
    fun provideSplashServices(retrofit: Retrofit): SplashServices =
            retrofit.create(SplashServices::class.java)

    @Provides
    @Singleton
    fun provideHomeServices(retrofit: Retrofit): HomePageServices =
            retrofit.create(HomePageServices::class.java)

    @Provides
    @Singleton
    fun provideHomeServicesV1(retrofit: Retrofit): HomeServicesV1 =
            retrofit.create(HomeServicesV1::class.java)

    @Provides
    @Singleton
    fun provideSplashServicesV1(retrofit: Retrofit): SplashServicesV1 =
            retrofit.create(SplashServicesV1::class.java)


    @Provides
    @Singleton
    fun provideChatServices(retrofit: Retrofit): ChatServices =
            retrofit.create(ChatServices::class.java)

    @Provides
    @Singleton
    fun provideFCMTokenServices(retrofit: Retrofit): FCMTokenServices =
            retrofit.create(FCMTokenServices::class.java)


    @Provides
    @Singleton
    fun provideSignUpAuthServices(retrofit: Retrofit) : SignUpAuthServices =
        retrofit.create(SignUpAuthServices::class.java)

    @Provides
    @Singleton
    fun provideOnboardingStageServices(retrofit: Retrofit) : OnboardingStageServices =
        retrofit.create(OnboardingStageServices::class.java)

    @Provides
    @Singleton
    fun provideCustomerGroupServices(retrofit: Retrofit) :  UserCategoryServices =
        retrofit.create(UserCategoryServices::class.java)

    @Provides
    @Singleton
    fun provideReferralCodeServices(retrofit: Retrofit) : ReferralCodeServices =
        retrofit.create(ReferralCodeServices::class.java)

    @Provides
    @Singleton
    fun provideUserDetailsServices(retrofit: Retrofit) :  UserDetailsServices =
        retrofit.create(UserDetailsServices::class.java)

    @Provides
    @Singleton
    fun provideUserAddressServices(retrofit: Retrofit): UserAddressServices =
        retrofit.create(UserAddressServices::class.java)


    @Provides
    @Singleton
    fun provideUserLocationServices(retrofit: Retrofit) : UserLocationServices =
        retrofit.create(UserLocationServices::class.java)

    @Provides
    @Singleton
    fun provideProvinceServices(retrofit: Retrofit) : ProvinceServices =
        retrofit.create(ProvinceServices::class.java)

    @Provides
    @Singleton
    fun provideDistrictServices(retrofit: Retrofit) :  DistrictServices =
        retrofit.create(DistrictServices::class.java)

    @Provides
    @Singleton
    fun provideSubDistrictServices(retrofit: Retrofit) : SubDistrictServices =
        retrofit.create(SubDistrictServices::class.java)

    @Provides
    @Singleton
    fun provideVillageServices(retrofit: Retrofit) :  VillageServices =
        retrofit.create(VillageServices::class.java)
}