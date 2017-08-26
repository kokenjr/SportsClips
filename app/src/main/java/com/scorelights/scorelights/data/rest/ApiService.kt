package com.scorelights.scorelights.data.rest

import com.scorelights.scorelights.data.rest.response.ClipsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by korji on 5/16/17.
 */
interface ApiService {
    @GET("posts")
    fun getClips(@Query("subreddit") subreddit: String, @Query("after") after: String,
                 @Query("time") time: String, @Query("sort") sort: String,
                 @Header("Cache-Control") cacheControl : String)
            : Call<ClipsResponse>
}