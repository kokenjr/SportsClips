package com.scorelights.scorelights.data.rest

import android.content.Context
import com.scorelights.scorelights.BuildConfig
import com.scorelights.scorelights.common.Constants
import com.scorelights.scorelights.data.rest.interceptor.SessionRequestInterceptor
import com.scorelights.scorelights.data.rest.response.ClipsResponse
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by korji on 5/16/17.
 */
class RestApi @Inject constructor(var context: Context?) {
    var apiService: ApiService

    init {


        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.REST_ENDPOINT_URL)
                .client(getOkHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private fun getOkHttpClient(context: Context?): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()

        clientBuilder.connectTimeout(Constants.REST_CLIENT_TIMEOUT, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(Constants.REST_CLIENT_TIMEOUT, TimeUnit.SECONDS)
        clientBuilder.readTimeout(Constants.REST_CLIENT_TIMEOUT, TimeUnit.SECONDS)

        if (context != null) {
            val cacheSize = 10 * 1024 * 1024 // 10 MB
            val cache = Cache(context.cacheDir, cacheSize.toLong())
            clientBuilder.cache(cache)
        }

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(interceptor)
        }

        clientBuilder.addInterceptor(SessionRequestInterceptor())
        return clientBuilder.build()
    }

    fun getClips(subreddit: String, after: String, time: String, sort: String,
                 cacheControl: String): Call<ClipsResponse> {
        return apiService.getClips(subreddit, after, time, sort, cacheControl)
    }
}