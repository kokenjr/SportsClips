package com.scorelights.scorelights.data.rest

import com.scorelights.scorelights.data.rest.response.ClipsResponse
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by korji on 5/16/17.
 */
@Singleton
class ClipsManager @Inject constructor(private val api: RestApi) {
    fun getClips(subreddit: String, after: String, time: String, sort: String, cacheControl: String): Observable<ClipsResponse> {
        return Observable.create {
            subscriber ->
            val callResponse = api.getClips(subreddit, after, time, sort, cacheControl)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val dataResponse = response.body()

                subscriber.onNext(dataResponse)
                subscriber.onCompleted()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}