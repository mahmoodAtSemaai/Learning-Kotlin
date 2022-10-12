package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.data.remoteSource.AuthServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AuthenticationServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ChatServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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
    fun provideOrderServices(retrofit: Retrofit): OrderServices =
        retrofit.create(OrderServices::class.java)

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
    fun provideOTPServices(retrofit: Retrofit): OTPServices =
        retrofit.create(OTPServices::class.java)


    @Provides
    @Singleton
    fun provideHomeServicesV1(retrofit: Retrofit): HomeServices =
        retrofit.create(HomeServices::class.java)

    @Provides
    @Singleton
    fun provideSplashServicesV1(retrofit: Retrofit): SplashServices =
        retrofit.create(SplashServices::class.java)


    @Provides
    @Singleton
    fun provideChatServices(retrofit: Retrofit): ChatServices =
        retrofit.create(ChatServices::class.java)

    @Provides
    @Singleton
    fun provideShippingMethodServices(retrofit: Retrofit): ShippingMethodServices =
        retrofit.create(ShippingMethodServices::class.java)

    @Provides
    @Singleton
    fun providePaymentAcquireServices(retrofit: Retrofit): PaymentAcquireServices =
        retrofit.create(PaymentAcquireServices::class.java)

    @Provides
    @Singleton
    fun providePaymentStatusServices(retrofit: Retrofit): PaymentStatusServices =
            retrofit.create(PaymentStatusServices::class.java)

    @Provides
    @Singleton
    fun providePaymentProcessingServices(retrofit: Retrofit): PaymentProcessingServices =
        retrofit.create(PaymentProcessingServices::class.java)

    @Provides
    @Singleton
    fun provideFCMTokenServices(retrofit: Retrofit): FCMTokenServices =
        retrofit.create(FCMTokenServices::class.java)


    @Provides
    @Singleton
    fun providePaymentTransferInstructionServices(retrofit: Retrofit): PaymentTransferInstructionServices =
            retrofit.create(PaymentTransferInstructionServices::class.java)


    @Provides
    @Singleton
    fun provideSignUpAuthServices(retrofit: Retrofit): SignUpAuthServices =
        retrofit.create(SignUpAuthServices::class.java)

    @Provides
    @Singleton
    fun provideOnboardingStageServices(retrofit: Retrofit): OnboardingStageServices =
        retrofit.create(OnboardingStageServices::class.java)

    @Provides
    @Singleton
    fun provideCustomerGroupServices(retrofit: Retrofit): UserCategoryServices =
        retrofit.create(UserCategoryServices::class.java)

    @Provides
    @Singleton
    fun provideReferralCodeServices(retrofit: Retrofit): ReferralCodeServices =
        retrofit.create(ReferralCodeServices::class.java)

    @Provides
    @Singleton
    fun provideUserDetailsServices(retrofit: Retrofit): UserServices =
        retrofit.create(UserServices::class.java)

    @Provides
    @Singleton
    fun provideUserAddressServices(retrofit: Retrofit): UserAddressServices =
        retrofit.create(UserAddressServices::class.java)


    @Provides
    @Singleton
    fun provideUserLocationServices(retrofit: Retrofit): UserLocationServices =
        retrofit.create(UserLocationServices::class.java)

    @Provides
    @Singleton
    fun provideProvinceServices(retrofit: Retrofit): ProvinceServices =
        retrofit.create(ProvinceServices::class.java)

    @Provides
    @Singleton
    fun provideDistrictServices(retrofit: Retrofit): DistrictServices =
        retrofit.create(DistrictServices::class.java)

    @Provides
    @Singleton
    fun provideSubDistrictServices(retrofit: Retrofit): SubDistrictServices =
        retrofit.create(SubDistrictServices::class.java)

    @Provides
    @Singleton
    fun provideVillageServices(retrofit: Retrofit): VillageServices =
        retrofit.create(VillageServices::class.java)

    @Provides
    @Singleton
    fun provideWishListServices(retrofit: Retrofit): WishListServices =
            retrofit.create(WishListServices::class.java)

    @Provides
    @Singleton
    fun provideCartServices(retrofit: Retrofit): CartServices =
            retrofit.create(CartServices::class.java)

    @Provides
    @Singleton
    fun provideUserDetailServices(retrofit: Retrofit): UserDetailServices =
            retrofit.create(UserDetailServices::class.java)

    @Provides
    @Singleton
    fun provideProductCategoriesServices(retrofit: Retrofit): CategoriesServices =
            retrofit.create(CategoriesServices::class.java)


    @Provides
    @Singleton
    fun provideBannerServices(retrofit: Retrofit): BannerServices =
            retrofit.create(BannerServices::class.java)

    @Provides
    @Singleton
    fun provideSellerServices(retrofit: Retrofit): SellerServices =
            retrofit.create(SellerServices::class.java)

    @Provides
    @Singleton
    fun provideProductServices(retrofit: Retrofit): ProductServices =
            retrofit.create(ProductServices::class.java)
}