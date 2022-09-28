package com.webkul.mobikul.odoo.core.data.remote

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.*
import com.webkul.mobikul.odoo.data.response.BaseOtpSignUpResponse
import com.webkul.mobikul.odoo.data.response.BaseUserOnboardingResponse
import com.webkul.mobikul.odoo.data.response.TermsAndConditionsResponse
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.core.data.response.BaseResponseNew
import com.webkul.mobikul.odoo.data.response.models.CartBaseResponse
import com.webkul.mobikul.odoo.model.ReferralResponse
import com.webkul.mobikul.odoo.model.chat.ChatBaseResponse
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.DistrictListResponse
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.SubDistrictListResponse
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.VillageListResponse
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

open class BaseRemoteDataSource @Inject constructor(
    private val gson: Gson,
    private val appPreferences: AppPreferences
) {


    suspend fun <S, T> safeApiCall(domainType: Class<S>, apiCall: suspend () -> T): Resource<S> {
        try {
            val apiResponse = apiCall.invoke()

            return if (apiResponse is BaseResponse) {
                if (apiResponse.isSuccess) {
                    if (apiResponse.isAccessDenied) {
                        appPreferences.clearCustomerData()
                        Resource.Failure(
                            failureStatus = FailureStatus.ACCESS_DENIED,
                            message = apiResponse.message
                        )
                    } else if (apiResponse.isUserOnboarded.not() && ((apiResponse is SplashScreenResponse) || (apiResponse is HomePageResponse))) {
                        appPreferences.isUserOnboarded = false
                        if (apiResponse.userId.isNotEmpty()) {
                            appPreferences.userId = apiResponse.userId
                            appPreferences.customerId = apiResponse.customerId
                        }
                        Resource.Failure(
                            failureStatus = FailureStatus.INCOMPLETE_ONBOARDING,
                            code = HTTP_ERROR_UNAUTHORIZED_REQUEST,
                            message = apiResponse.message
                        )
                    } else if (apiResponse.isUserApproved.not()) {
                        appPreferences.isUserApproved = false
                        Resource.Failure(
                            failureStatus = FailureStatus.USER_UNAPPROVED,
                            code = HTTP_ERROR_UNAUTHORIZED_REQUEST,
                            message = apiResponse.message
                        )
                    } else {
                        val parsedResponse = gson.fromJson(gson.toJson(apiResponse), domainType)
                        Resource.Success(parsedResponse)
                    }
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message
                    )
                }
            } else if (apiResponse is ChatBaseResponse<*>) {
                if (apiResponse.success) {
                    val parsedResponse = gson.fromJson(gson.toJson(apiResponse.data), domainType)
                    Resource.Success(parsedResponse)
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message
                    )
                }
            } else if (apiResponse is BaseResponseNew<*>) {
                if (apiResponse.status) {
                    val parsedResponse = gson.fromJson(gson.toJson(apiResponse.data), domainType)
                    Resource.Success(parsedResponse)
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message
                    )
                }
            } else if (apiResponse is BaseUserOnboardingResponse<*>) {
                val isSuccess = when (val extractedStatusCode = apiResponse.statusCode) {
                    is String -> {
                        extractedStatusCode.toInt() == HTTP_RESPONSE_OK ||
                                extractedStatusCode.toInt() == HTTP_RESPONSE_RESOURCE_CREATED
                    }
                    else -> {
                        false
                    }
                }
                if (apiResponse.statusCode == HTTP_RESPONSE_RESOURCE_CREATED.toString() ||
                    apiResponse.statusCode == HTTP_RESPONSE_OK.toString() || isSuccess
                ) {
                    val parsedResponse = gson.fromJson(gson.toJson(apiResponse.result), domainType)
                    Resource.Success(parsedResponse)
                } else if (apiResponse.statusCode == HTTP_RESOURCE_NOT_FOUND.toString()) {
                    Resource.Failure(
                        failureStatus = FailureStatus.OTHER,
                        code = HTTP_RESOURCE_NOT_FOUND,
                        message = apiResponse.message
                    )
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        code = HTTP_ERROR_UNAUTHORIZED_REQUEST,
                        message = apiResponse.message
                    )
                }
            } else if ((apiResponse is StateListResponse)) {
                if (apiResponse.status == HTTP_RESPONSE_OK) {
                    val parsedResponse = gson.fromJson(gson.toJson(apiResponse), domainType)
                    Resource.Success(parsedResponse)
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message
                    )
                }
            } else if ((apiResponse is DistrictListResponse)) {
                if (apiResponse.status == HTTP_RESPONSE_OK.toString()) {
                    val parsedResponse = gson.fromJson(gson.toJson(apiResponse), domainType)
                    Resource.Success(parsedResponse)
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message
                    )
                }
            } else if ((apiResponse is SubDistrictListResponse)) {
                if (apiResponse.status == HTTP_RESPONSE_OK.toString()) {
                    val parsedResponse = gson.fromJson(gson.toJson(apiResponse), domainType)
                    Resource.Success(parsedResponse)
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message
                    )
                }
            } else if ((apiResponse is VillageListResponse)) {
                if (apiResponse.status == HTTP_RESPONSE_OK.toString()) {
                    val parsedResponse = gson.fromJson(gson.toJson(apiResponse), domainType)
                    Resource.Success(parsedResponse)
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message
                    )
                }
            } else if ((apiResponse is ReferralResponse)) {
                if (apiResponse.status == HTTP_RESPONSE_OK) {
                    val parsedResponse = gson.fromJson(gson.toJson(apiResponse), domainType)
                    Resource.Success(parsedResponse)
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.OTHER,
                        message = apiResponse.message
                    )
                }
            } else if (apiResponse is TermsAndConditionsResponse) {
                if (apiResponse.success) {
                    val parsedResponse = gson.fromJson(gson.toJson(apiResponse), domainType)
                    Resource.Success(parsedResponse)
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message
                    )
                }
            } else if (apiResponse is CartBaseResponse<*>) {
                if (apiResponse.statusCode == HTTP_RESPONSE_OK || apiResponse.statusCode == HTTP_RESPONSE_RESOURCE_CREATED) {
                    val parsedResponse = gson.fromJson(gson.toJson(apiResponse.result), domainType)
                    Resource.Success(parsedResponse)
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message,
                        code = apiResponse.statusCode.toInt()
                    )
                }
            } else {
                val parsedResponse = gson.fromJson(gson.toJson(apiResponse), domainType)
                Resource.Success(parsedResponse)
            }

        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    when {
                        throwable.code() == HTTP_ERROR_UNABLE_TO_PROCESS_REQUEST -> {
                            val jsonErrorObject =
                                JSONObject(throwable.response()?.errorBody()?.string() ?: "")
                            val apiResponse = jsonErrorObject.toString()

                            return Resource.Failure(
                                FailureStatus.API_FAIL,
                                throwable.code(),
                                apiResponse
                            )
                        }
                        throwable.code() == HTTP_ERROR_UNAUTHORIZED_REQUEST -> {
                            val jsonErrorObject =
                                JSONObject(throwable.response()?.errorBody()?.string() ?: "")
                            val apiResponse = jsonErrorObject.toString()

                            return Resource.Failure(
                                FailureStatus.API_FAIL,
                                throwable.code(),
                                apiResponse
                            )
                        }
                        else -> {
                            return if (throwable.response()?.errorBody()!!.charStream().readText()
                                    .isEmpty()
                            ) {
                                Resource.Failure(FailureStatus.API_FAIL, throwable.code())
                            } else {
                                try {
                                    val jsonErrorObject =
                                        JSONObject(
                                            throwable.response()?.errorBody()?.string()
                                                ?: ""
                                        )
                                    val apiResponse = jsonErrorObject.toString()

                                    Resource.Failure(
                                        FailureStatus.API_FAIL,
                                        throwable.code(),
                                        apiResponse
                                    )
                                } catch (ex: Throwable) {
                                    Resource.Failure(FailureStatus.API_FAIL, throwable.code())
                                }
                            }
                        }
                    }
                }

                is UnknownHostException -> {
                    return Resource.Failure(FailureStatus.NO_INTERNET)
                }

                is ConnectException -> {
                    return Resource.Failure(FailureStatus.NO_INTERNET)
                }

                is IOException -> {
                    return Resource.Failure(
                        FailureStatus.API_FAIL,
                        HTTP_ERROR_UNABLE_TO_PROCESS_REQUEST,
                        throwable.message
                    )
                }

                else -> {
                    return Resource.Failure(FailureStatus.OTHER)
                }
            }
        }
    }

    //TODO-> Need to remove once everybody migrate to above one
    @Deprecated("Need to create Entity correspond to responses.This will be deleted")
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        try {
            val apiResponse = apiCall.invoke()

            return if (apiResponse is BaseResponse) {
                if (apiResponse.isSuccess) {
                    if (apiResponse.isAccessDenied) {
                        Resource.Failure(
                            failureStatus = FailureStatus.ACCESS_DENIED,
                            message = apiResponse.message
                        )
                    } else if (apiResponse.isUserOnboarded.not()) {
                        appPreferences.isUserOnboarded = false
                        if (apiResponse.userId.isNotEmpty()) {
                            appPreferences.userId = apiResponse.userId
                            appPreferences.customerId = apiResponse.customerId
                        }
                        Resource.Failure(
                            failureStatus = FailureStatus.INCOMPLETE_ONBOARDING,
                            code = HTTP_ERROR_UNAUTHORIZED_REQUEST,
                            message = apiResponse.message
                        )
                    } else if (apiResponse.isUserApproved.not()) {
                        appPreferences.isUserApproved = false
                        Resource.Failure(
                            failureStatus = FailureStatus.USER_UNAPPROVED,
                            code = HTTP_ERROR_UNAUTHORIZED_REQUEST,
                            message = apiResponse.message
                        )
                    } else {
                        Resource.Success(apiResponse)
                    }
                } else if ((apiResponse is StateListResponse)) {
                    if (apiResponse.status == HTTP_RESPONSE_OK) {
                        Resource.Success(apiResponse)
                    } else {
                        Resource.Failure(
                            failureStatus = FailureStatus.API_FAIL,
                            message = apiResponse.message
                        )
                    }
                } else if ((apiResponse is DistrictListResponse)) {
                    if (apiResponse.status == HTTP_RESPONSE_OK.toString()) {
                        Resource.Success(apiResponse)
                    } else {
                        Resource.Failure(
                            failureStatus = FailureStatus.API_FAIL, message = apiResponse.message
                        )
                    }
                } else if ((apiResponse is SubDistrictListResponse)) {
                    if (apiResponse.status == HTTP_RESPONSE_OK.toString()) {
                        Resource.Success(apiResponse)
                    } else {
                        Resource.Failure(
                            failureStatus = FailureStatus.API_FAIL,
                            message = apiResponse.message
                        )
                    }
                } else if ((apiResponse is VillageListResponse)) {
                    if (apiResponse.status == HTTP_RESPONSE_OK.toString()) {
                        Resource.Success(apiResponse)
                    } else {
                        Resource.Failure(
                            failureStatus = FailureStatus.API_FAIL,
                            message = apiResponse.message
                        )
                    }
                } else if (apiResponse is ChatBaseResponse<*>) {
                    if (apiResponse.success) {
                        Resource.Success(apiResponse)
                    } else {
                        Resource.Failure(
                            failureStatus = FailureStatus.API_FAIL,
                            message = apiResponse.message
                        )
                    }
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message
                    )
                }
            } else if (apiResponse is BaseOtpSignUpResponse<*>) {
                if (apiResponse.statusCode == HTTP_RESPONSE_OK.toString() ||
                    apiResponse.statusCode == HTTP_RESPONSE_RESOURCE_CREATED.toString()) {
                    Resource.Success(apiResponse)
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message,
                        code = apiResponse.statusCode.toInt()
                    )
                }
            } else if (apiResponse is TermsAndConditionsResponse) {
                if (apiResponse.success) {
                    Resource.Success(apiResponse)
                } else {
                    Resource.Failure(
                        failureStatus = FailureStatus.API_FAIL,
                        message = apiResponse.message
                    )
                }
            } else {
                Resource.Success(apiResponse)
            }

        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    when {
                        throwable.code() == HTTP_ERROR_UNABLE_TO_PROCESS_REQUEST -> {
                            val jsonErrorObject =
                                JSONObject(throwable.response()?.errorBody()?.string() ?: "")
                            val apiResponse = jsonErrorObject.toString()

                            return Resource.Failure(
                                FailureStatus.API_FAIL,
                                throwable.code(),
                                apiResponse
                            )
                        }
                        throwable.code() == HTTP_ERROR_UNAUTHORIZED_REQUEST -> {
                            val jsonErrorObject =
                                JSONObject(throwable.response()?.errorBody()?.string() ?: "")
                            val apiResponse = jsonErrorObject.toString()

                            return Resource.Failure(
                                FailureStatus.API_FAIL,
                                throwable.code(),
                                apiResponse
                            )
                        }
                        else -> {
                            return if (throwable.response()?.errorBody()!!.charStream().readText()
                                    .isEmpty()
                            ) {
                                Resource.Failure(FailureStatus.API_FAIL, throwable.code())
                            } else {
                                try {
                                    val jsonErrorObject =
                                        JSONObject(
                                            throwable.response()?.errorBody()?.string()
                                                ?: ""
                                        )
                                    val apiResponse = jsonErrorObject.toString()

                                    Resource.Failure(
                                        FailureStatus.API_FAIL,
                                        throwable.code(),
                                        apiResponse
                                    )
                                } catch (ex: Throwable) {
                                    Resource.Failure(FailureStatus.API_FAIL, throwable.code())
                                }
                            }
                        }
                    }
                }

                is UnknownHostException -> {
                    return Resource.Failure(FailureStatus.NO_INTERNET)
                }

                is ConnectException -> {
                    return Resource.Failure(FailureStatus.NO_INTERNET)
                }

                is IOException -> {
                    return Resource.Failure(
                        FailureStatus.API_FAIL,
                        HTTP_ERROR_UNABLE_TO_PROCESS_REQUEST,
                        throwable.message
                    )
                }

                else -> {
                    return Resource.Failure(FailureStatus.OTHER)
                }
            }
        }
    }
}