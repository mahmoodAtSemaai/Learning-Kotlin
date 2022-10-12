package com.webkul.mobikul.odoo.core.di

import android.content.Context
import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.data.remoteSource.AuthServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.*
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun authRemoteDataSource(
        authServices: AuthServices,
        @ApplicationContext context: Context,
        gson: Gson,
        appPreferences: AppPreferences
    ): AuthRemoteDataSource =
        AuthRemoteDataSource(authServices, context, gson, appPreferences)

    @Provides
    @Singleton
    fun addressRemoteDataSource(
        addressServices: AddressServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): AddressRemoteDataSource =
        AddressRemoteDataSource(addressServices, gson, appPreferences)

    @Provides
    @Singleton
    fun addressCountryStateRemoteDataSource(
        countryStateServices: CountryStateServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): CountryStateRemoteDataSource =
        CountryStateRemoteDataSource(countryStateServices, gson, appPreferences)

    @Provides
    @Singleton
    fun orderRemoteDataSource(
        orderServices: OrderServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): OrderRemoteDataSource = OrderRemoteDataSource(orderServices, gson, appPreferences)

    @Provides
    @Singleton
    fun termsConditionRemoteDataSource(
        termsConditionServices: TermsConditionServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): TermsConditionRemoteDataSource =
        TermsConditionRemoteDataSource(termsConditionServices, gson, appPreferences)

    @Provides
    @Singleton
    fun sellerTermsConditionRemoteDataSource(
        termsConditionServices: TermsConditionServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): SellerTermsConditionRemoteDataSource =
        SellerTermsConditionRemoteDataSource(termsConditionServices, gson, appPreferences)

    @Provides
    @Singleton
    fun authenticationPasswordRemoteDataSource(
        authenticationServices: AuthenticationServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): AuthenticationPasswordRemoteDataSource =
        AuthenticationPasswordRemoteDataSource(authenticationServices, appPreferences, gson)

    @Provides
    @Singleton
    fun authenticationOtpRemoteDataSource(
        authenticationServices: AuthenticationServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): AuthenticationOtpRemoteDataSource =
        AuthenticationOtpRemoteDataSource(authenticationServices, appPreferences, gson)

    @Provides
    @Singleton
    fun otpRemoteDataSource(
        otpServices: OTPServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): OtpRemoteDataSource =
        OtpRemoteDataSource(otpServices, appPreferences, gson)

    @Provides
    @Singleton
    fun phoneNumberRemoteDataSource(
        authenticationServices: AuthenticationServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): PhoneNumberRemoteDataSource =
        PhoneNumberRemoteDataSource(authenticationServices, appPreferences, gson)

    @Provides
    @Singleton
    fun shippingMethodRemoteDataSource(
        shippingMethodServices: ShippingMethodServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): ShippingMethodRemoteDataSource = ShippingMethodRemoteDataSource(shippingMethodServices, gson, appPreferences)

    @Provides
    @Singleton
    fun paymentAcquireRemoteDataSource(
        paymentAcquireServices: PaymentAcquireServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): PaymentAcquireRemoteDataSource = PaymentAcquireRemoteDataSource(paymentAcquireServices, gson, appPreferences)

    @Provides
    @Singleton
    fun userAnalyticsRemoteDataSource(
        splashServices: SplashServices,
        @ApplicationContext context: Context,
        gson: Gson,
        appPreferences: AppPreferences
    ): UserAnalyticsRemoteDataSource =
        UserAnalyticsRemoteDataSource(splashServices, context, gson, appPreferences)

    @Provides
    @Singleton
    fun userHomeRemoteDataSource(
        homeServicesV1: HomeServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): HomeRemoteDataSource = HomeRemoteDataSource(homeServicesV1, gson, appPreferences)

    @Provides
    @Singleton
    fun userSplashRemoteDataSourceV1(
        splashServices: SplashServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): SplashRemoteDataSource = SplashRemoteDataSource(splashServices, gson, appPreferences)


    @Provides
    @Singleton
    fun chatChannelRemoteDataSource(
        chatServices: ChatServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): ChatChannelRemoteDataSource = ChatChannelRemoteDataSource(chatServices, gson, appPreferences)

    @Provides
    @Singleton
    fun paymentStatusRemoteDataSource(
            paymentStatusServices: PaymentStatusServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): PaymentStatusRemoteDataSource = PaymentStatusRemoteDataSource(paymentStatusServices, gson, appPreferences)

    @Provides
    @Singleton
    fun paymentProcessingRemoteDataSource(
        paymentProcessingServices: PaymentProcessingServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): PaymentProcessingRemoteDataSource = PaymentProcessingRemoteDataSource(paymentProcessingServices, gson, appPreferences)

    @Provides
    @Singleton
    fun signUpAuthRemoteDataSource(
        signUpAuthServices: SignUpAuthServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): SignUpAuthRemoteDataSource =
        SignUpAuthRemoteDataSource(signUpAuthServices, gson, appPreferences)

    @Provides
    @Singleton
    fun onboardingStageRemoteDataSource(
        onboardingStageServices: OnboardingStageServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): OnboardingStageRemoteDataSource =
        OnboardingStageRemoteDataSource(onboardingStageServices, gson, appPreferences)

    @Provides
    @Singleton
    fun userOnboardingStageRemoteDataSource(
        userServices: UserServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): UserOnboardingStageRemoteDataSource =
        UserOnboardingStageRemoteDataSource(userServices, gson, appPreferences)

    @Provides
    @Singleton
    fun customerGroupeRemoteDataSource(
        userCategoryServices: UserCategoryServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): CustomerGroupRemoteDataSource =
        CustomerGroupRemoteDataSource(userCategoryServices, gson, appPreferences)

    @Provides
    @Singleton
    fun userCustomerGroupRemoteDataSource(
        userCategoryServices: UserCategoryServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): UserCustomerGroupRemoteDataSource =
        UserCustomerGroupRemoteDataSource(userCategoryServices, gson, appPreferences)

    @Provides
    @Singleton
    fun userRemoteDataSource(
        userServices: UserServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): UserRemoteDataSource =
        UserRemoteDataSource(userServices, gson, appPreferences)

    @Provides
    @Singleton
    fun referralCodeRemoteDataSource(
        referralCodeServices: ReferralCodeServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): ReferralCodeRemoteDataSource =
        ReferralCodeRemoteDataSource(referralCodeServices, gson, appPreferences)

    @Provides
    @Singleton
    fun userAddressRemoteDataSource(
        userAddressServices: UserAddressServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): UserAddressRemoteDataSource =
        UserAddressRemoteDataSource(userAddressServices, gson, appPreferences)

    @Provides
    @Singleton
    fun userLocationRemoteDataSource(
        userLocationServices: UserLocationServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): UserLocationRemoteDataSource =
        UserLocationRemoteDataSource(userLocationServices, gson, appPreferences)

    @Provides
    @Singleton
    fun provinceRemoteDataSource(
        provinceServices: ProvinceServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): ProvinceRemoteDataSource = ProvinceRemoteDataSource(provinceServices, gson, appPreferences)

    @Provides
    @Singleton
    fun districtRemoteDataSource(
        districtServices: DistrictServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): DistrictRemoteDataSource = DistrictRemoteDataSource(districtServices, gson, appPreferences)

    @Provides
    @Singleton
    fun subDistrictRemoteDataSource(
        subDistrictServices: SubDistrictServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): SubDistrictRemoteDataSource =
        SubDistrictRemoteDataSource(subDistrictServices, gson, appPreferences)

    @Provides
    @Singleton
    fun villageRemoteDataSource(
        villageServices: VillageServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): VillageRemoteDataSource = VillageRemoteDataSource(villageServices, gson, appPreferences)

    @Provides
    @Singleton
    fun fcmTokenRemoteDataSource(
        fcmTokenServices: FCMTokenServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): FCMTokenRemoteDataSource =
        FCMTokenRemoteDataSource(fcmTokenServices, gson, appPreferences)

    @Provides
    @Singleton
    fun paymentTransferInstructionRemoteDataSource(
            paymentTransferInstructionServices: PaymentTransferInstructionServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): PaymentTransferInstructionRemoteDataSource = PaymentTransferInstructionRemoteDataSource(paymentTransferInstructionServices, gson, appPreferences)

    @Provides
    @Singleton
    fun updateCartRemoteDataSource(
            cartServices: CartServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): CartRemoteDataSource = CartRemoteDataSource(cartServices, gson, appPreferences)

    @Provides
    @Singleton
    fun wishListRemoteDataSource(
            wishListServices: WishListServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): WishListRemoteDataSource = WishListRemoteDataSource(wishListServices, gson, appPreferences)

    @Provides
    @Singleton
    fun userDetailRemoteDataSource(
        userDetailServices: UserDetailServices,
        gson: Gson,
        appPreferences: AppPreferences
    ): UserDetailRemoteDataSource = UserDetailRemoteDataSource(userDetailServices, gson, appPreferences)


    @Provides
    @Singleton
    fun productCategoriesRemoteDataSource(
            categoriesServices: CategoriesServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): CategoriesRemoteDataSource = CategoriesRemoteDataSource(categoriesServices, gson, appPreferences)

    @Provides
    @Singleton
    fun bannerRemoteDataSource(
            bannerServices: BannerServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): BannerRemoteDataSource = BannerRemoteDataSource(bannerServices, gson, appPreferences)

    @Provides
    @Singleton
    fun sellerRemoteDataSource(
            sellerServices: SellerServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): SellerRemoteDataSource = SellerRemoteDataSource(sellerServices, gson, appPreferences)

    @Provides
    @Singleton
    fun sellerProductsRemoteDataSource(
            productServices: ProductServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): SellerProductsRemoteDataSource = SellerProductsRemoteDataSource(productServices, gson, appPreferences)

    @Provides
    @Singleton
    fun searchRemoteDataSource(
            productServices: ProductServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): SearchRemoteDataSource = SearchRemoteDataSource(productServices, gson, appPreferences)

    @Provides
    @Singleton
    fun productDetailsRemoteDataSource(
            productServices: ProductServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): ProductDetailsRemoteDataSource = ProductDetailsRemoteDataSource(productServices, gson, appPreferences)

    @Provides
    @Singleton
    fun productSellersRemoteDataSource(
            productServices: ProductServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): ProductSellersRemoteDataSource = ProductSellersRemoteDataSource(productServices, gson, appPreferences)
}