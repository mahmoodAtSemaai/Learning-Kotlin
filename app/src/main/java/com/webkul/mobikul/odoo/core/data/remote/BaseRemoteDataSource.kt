package com.webkul.mobikul.odoo.core.data.remote

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.HTTP_ERROR_UNABLE_TO_PROCESS_REQUEST
import com.webkul.mobikul.odoo.core.utils.HTTP_ERROR_UNAUTHORIZED_REQUEST
import com.webkul.mobikul.odoo.core.utils.Resource
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

open class BaseRemoteDataSource @Inject constructor() {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        try {
            val apiResponse = apiCall.invoke()

            return Resource.Success(apiResponse)

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
                                            throwable.response()?.errorBody()?.string() ?: ""
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

                else -> {
                    return Resource.Failure(FailureStatus.OTHER)
                }
            }
        }
    }
}