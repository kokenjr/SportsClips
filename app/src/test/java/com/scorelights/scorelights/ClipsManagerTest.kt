package com.scorelights.scorelights

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.scorelights.scorelights.data.domain.Clip
import com.scorelights.scorelights.data.rest.ApiService
import com.scorelights.scorelights.data.rest.ClipsManager
import com.scorelights.scorelights.data.rest.RestApi
import com.scorelights.scorelights.data.rest.response.ClipsResponse
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import rx.observers.TestSubscriber
import org.mockito.Mock
import retrofit2.Response


/**
 * Created by korji on 5/16/17.
 */
class ClipsManagerTest {
    @Mock var fakeContext: Context? = null
    var restApi = RestApi(fakeContext)
    var testSubscriber = TestSubscriber<ClipsResponse>()
    var callMock = mock<Call<ClipsResponse>>()

    @Before
    fun setup() {
        testSubscriber = TestSubscriber<ClipsResponse>()
        callMock = mock<Call<ClipsResponse>>()
        val apiMock = mock<ApiService>()
        whenever(apiMock.getClips(any(), any(), any(), any(), any())).thenReturn(callMock)
    }

    @Test
    fun testSuccess_getClips() {
        val clipsResponse = ClipsResponse(listOf(), "" )
        val response = Response.success(clipsResponse)

        whenever(callMock.execute()).thenReturn(response)

        val clipsManager = ClipsManager(restApi)
        clipsManager.getClips("soccer", "", "", "", "").subscribe(testSubscriber)

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertCompleted()
    }

    @Test
    fun testSuccess_getOneClip() {
        val clip = Clip(
                "id",
                "title",
                "url",
                "link",
                1,
                "thumbnail",
                1,
                1,
                12345,
                "author",
                "shareUrl",
                false,
                123,
                0,
                false
        )
        val clipsResponse = ClipsResponse(listOf(clip), "")
        val response = Response.success(clipsResponse)

        whenever(callMock.execute()).thenReturn(response)

        val newsManager = ClipsManager(restApi)
        newsManager.getClips("soccer", "", "", "", "").subscribe(testSubscriber)

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertCompleted()

        assert(testSubscriber.onNextEvents[0].clips[0].author == clip.author)
        assert(testSubscriber.onNextEvents[0].clips[0].title == clip.title)
    }

    @Test
    fun testError_getClips() {
        val responseError = Response.error<ClipsResponse>(500,
                ResponseBody.create(MediaType.parse("application/json"), ""))

        whenever(callMock.execute()).thenReturn(responseError)

        val clipsManager = ClipsManager(restApi)
        clipsManager.getClips("", "", "", "", "").subscribe(testSubscriber)

        assert(testSubscriber.onErrorEvents.size == 1)
    }
}