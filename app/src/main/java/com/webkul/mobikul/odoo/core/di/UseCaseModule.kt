package com.webkul.mobikul.odoo.core.di


import android.content.Context
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.NetworkManager
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.domain.repository.*
import com.webkul.mobikul.odoo.domain.usecase.auth.*
import com.webkul.mobikul.odoo.domain.usecase.chat.CreateChatChannelUseCase
import com.webkul.mobikul.odoo.domain.usecase.home.HomeLocalDataUseCase
import com.webkul.mobikul.odoo.domain.usecase.home.HomeUseCase
import com.webkul.mobikul.odoo.domain.usecase.network.IsNetworkUseCase
import com.webkul.mobikul.odoo.domain.usecase.session.IsValidSessionUseCase
import com.webkul.mobikul.odoo.domain.usecase.splash.SplashLocalDataUseCase
import com.webkul.mobikul.odoo.domain.usecase.splash.SplashUseCase
import com.webkul.mobikul.odoo.features.authentication.domain.repo.AuthenticationRepository
import com.webkul.mobikul.odoo.features.authentication.domain.repo.HomePageRepository
import com.webkul.mobikul.odoo.features.authentication.domain.repo.SplashPageRepository
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.*
import com.webkul.mobikul.odoo.features.onboarding.domain.usecase.CountdownTimerUseCase
import com.webkul.mobikul.odoo.features.onboarding.domain.usecase.OnboardingUseCase
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
            countryStateRepository: CountryStateRepository
    ): CountryStateUseCase = CountryStateUseCase(countryStateRepository)

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

    @Provides
    @Singleton
    fun provideVerifyPhoneNumberUseCase(): VerifyPhoneNumberUseCase =
            VerifyPhoneNumberUseCase()

    @Provides
    @Singleton
    fun provideContinuePhoneNumberUseCase(authenticationRepository: AuthenticationRepository): ContinuePhoneNumberUseCase =
            ContinuePhoneNumberUseCase(authenticationRepository)

    @Provides
    @Singleton
    fun provideLoginPasswordUseCase(
            authenticationRepository: AuthenticationRepository,
            appPreferences: AppPreferences
    ): LoginPasswordUseCase =
            LoginPasswordUseCase(authenticationRepository, appPreferences)


    @Provides
    @Singleton
    fun provideGenerateOtpUseCase(authenticationRepository: AuthenticationRepository): GenerateOtpUseCase =
            GenerateOtpUseCase(authenticationRepository)

    @Provides
    @Singleton
    fun provideVerifyOtpUseCase(
            authenticationRepository: AuthenticationRepository,
            appPreferences: AppPreferences
    ): VerifyOtpUseCase =
            VerifyOtpUseCase(authenticationRepository, appPreferences)

    @Provides
    @Singleton
    fun provideSplashPageUseCase(splashPageRepository: SplashPageRepository): SplashPageUseCase =
            SplashPageUseCase(splashPageRepository)

    @Provides
    @Singleton
    fun provideHomePageUseCase(homePageRepository: HomePageRepository): HomePageDataUseCase =
            HomePageDataUseCase(homePageRepository)

    @Provides
    @Singleton
    fun provideCountdownTimerUseCase(): CountdownTimerUseCase = CountdownTimerUseCase()

    @Provides
    @Singleton
    fun provideOnboardingUseCase(resourcesProvider: ResourcesProvider): OnboardingUseCase =
            OnboardingUseCase(resourcesProvider)

    @Provides
    @Singleton
    fun provideCreateChatChannelUseCase(
            chatChannelRepository: ChatChannelRepository,
            appPreferences: AppPreferences
    ): CreateChatChannelUseCase =
            CreateChatChannelUseCase(chatChannelRepository, appPreferences)

    @Provides
    @Singleton
    fun provideIsFirstTimeUseCase(
            appPreferences: AppPreferences
    ): IsFirstTimeUseCase = IsFirstTimeUseCase(appPreferences)

    @Provides
    @Singleton
    fun provideHomeDataUseCase(
            homeDataRepository: HomeDataRepository,
            homeLocalDataUseCase: HomeLocalDataUseCase
    ): HomeUseCase = HomeUseCase(homeDataRepository, homeLocalDataUseCase)

    @Provides
    @Singleton
    fun provideSplashDataUseCase(
            splashDataRepository: SplashDataRepository,
            splashLocalDataUseCase: SplashLocalDataUseCase
    ): SplashUseCase = SplashUseCase(splashDataRepository, splashLocalDataUseCase)

    @Provides
    @Singleton
    fun provideHomeLocalDataUseCase(
            sqlLiteDbHelper: SqlLiteDbHelper
    ): HomeLocalDataUseCase = HomeLocalDataUseCase(sqlLiteDbHelper)

    @Provides
    @Singleton
    fun provideSplashLocalDataUseCase(
            sqlLiteDbHelper: SqlLiteDbHelper
    ): SplashLocalDataUseCase = SplashLocalDataUseCase(sqlLiteDbHelper)

    @Provides
    @Singleton
    fun provideIsNetworkUseCase(
            networkManager: NetworkManager
    ): IsNetworkUseCase = IsNetworkUseCase(networkManager)


    @Provides
    @Singleton
    fun provideIsUserAuthorisedUseCase(
            appPreferences: AppPreferences
    ): IsUserAuthorisedUseCase = IsUserAuthorisedUseCase(appPreferences)

    @Provides
    @Singleton
    fun provideIsUserApprovedUseCase(
            appPreferences: AppPreferences
    ): IsUserApprovedUseCase = IsUserApprovedUseCase(appPreferences)

    @Provides
    @Singleton
    fun provideIsValidSessionUseCase(
            isUserAuthorisedUseCase: IsUserAuthorisedUseCase,
            isUserApprovedUseCase: IsUserApprovedUseCase
    ): IsValidSessionUseCase = IsValidSessionUseCase(isUserAuthorisedUseCase, isUserApprovedUseCase)
}