package com.webkul.mobikul.odoo.core.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private const val DEFAULT_CONNECT_TIMEOUT_IN_SEC: Long = 90
    private const val DEFAULT_WRITE_TIMEOUT_IN_SEC: Long = 90
    private const val DEFAULT_READ_TIMEOUT_IN_SEC: Long = 90

    private const val AUTHORIZATION = "Authorization"
    private const val CONTENT_TYPE = "Content-Type"
    private const val TEXT_HTML = " text/plain; charset=UTF-8"
    private const val SOCIAL_LOGIN = "SocialLogin"
    private const val LOGIN = "Login"
    private const val LANGUAGE = "lang"
    private const val AUTH_TOKEN = "auth"

    @Provides
    @Singleton
    fun provideInterceptor(appPreferences: AppPreferences) =
        Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.addHeader(AUTHORIZATION, BuildConfig.BASIC_AUTH_KEY)
                .addHeader(CONTENT_TYPE, TEXT_HTML)

            if (appPreferences.authToken?.isEmpty() == true) {
                if (appPreferences.isLoggedIn) {
                    if (appPreferences.isSocialLoggedIn)
                        builder.addHeader(SOCIAL_LOGIN, appPreferences.customerLoginToken ?: "")
                    else
                        builder.addHeader(LOGIN, appPreferences.customerLoginToken ?: "")
                }
            } else {
                builder.addHeader(AUTH_TOKEN, appPreferences.authToken ?: "")
            }
            if (!appPreferences.languageCode.isNullOrEmpty()) {
                builder.addHeader(LANGUAGE, appPreferences.languageCode ?: "")
            }

            var response = chain.proceed(
                builder.build()
            )
            val body = response.peekBody(Long.MAX_VALUE).string()
            try {
                if (response.isSuccessful) {
                    val jsonResponse = JSONObject(body)
                    if (jsonResponse.has("success")) {
                        val isSuccess = jsonResponse.optBoolean("success")
                        if (!isSuccess) {
                            val message = jsonResponse.optString("message")
                            throw IOException(message)
                        }
                    }
                }

            } catch (e: Exception) {
                throw e
            }
            response
        }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headersInterceptor: Interceptor,
        logging: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .readTimeout(DEFAULT_READ_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                .addInterceptor(headersInterceptor)
                .addNetworkInterceptor(logging)
                .build()
        } else {
            OkHttpClient.Builder()
                .readTimeout(DEFAULT_READ_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                .addInterceptor(headersInterceptor)
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .serializeNulls() // To allow sending null values
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .build()
}