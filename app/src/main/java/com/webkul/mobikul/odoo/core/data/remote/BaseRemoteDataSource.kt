package com.webkul.mobikul.odoo.core.data.remote

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.HTTP_ERROR_UNABLE_TO_PROCESS_REQUEST
import com.webkul.mobikul.odoo.core.utils.HTTP_ERROR_UNAUTHORIZED_REQUEST
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.chat.ChatBaseResponse
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
                    } else if (apiResponse.isUserApproved.not()) {
                        appPreferences.isUserApproved = false
                        Resource.Failure(
                                failureStatus = FailureStatus.USER_UNAPPROVED,
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
                    val parsedResponse = gson.fromJson(gson.toJson(apiResponse), domainType)
                    Resource.Success(parsedResponse)
                } else {
                    Resource.Failure(
                            failureStatus = FailureStatus.API_FAIL,
                            message = apiResponse.message
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
                    } else if (apiResponse.isUserApproved.not()) {
                        appPreferences.isUserApproved = false
                        Resource.Failure(
                                failureStatus = FailureStatus.USER_UNAPPROVED,
                                message = apiResponse.message
                        )
                    } else {
                        Resource.Success(apiResponse)
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