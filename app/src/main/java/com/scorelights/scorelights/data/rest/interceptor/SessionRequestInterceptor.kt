package com.scorelights.scorelights.data.rest.interceptor

import com.scorelights.scorelights.common.Constants
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by korji on 7/5/17.
 */
class SessionRequestInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader("API-Auth", Constants.API_AUTH).build()
        return chain.proceed(request)
    }
}