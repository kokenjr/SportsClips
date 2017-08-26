package com.scorelights.scorelights.common

/**
 * Created by korji on 5/16/17.
 */
object Constants {
    //REST
    val API_AUTH = "scorelights:5CoR3CL1P5"
    val DEV_ENDPOINT_URL = "http://0a935cbd.ngrok.io/api/"
    val PROD_ENDPOINT_URL = "http://www.scorelights.club/api/"
    val REST_ENDPOINT_URL = PROD_ENDPOINT_URL
    val REST_CLIENT_TIMEOUT: Long = 0

    //Bundle Key Name
    val EXTRA_SUBREDDIT = "subreddit"
    val EXTRA_CLIP = "clip"
    val EXTRA_TIME = "time"
    val EXTRA_SORT = "sort"

    //Request Code
    val FULLSCREEN_REQUEST_CODE = 0

    //Adapter Type
    val CLIPS = 1
    val LOADING = 2

    val SORT_NEW = "new"
    val SORT_TOP = "top"
    val FILTER_HOUR = "hour"
    val FILTER_DAY = "day"
    val FILTER_WEEK = "week"
    val FILTER_MONTH = "month"
    val FILTER_YEAR = "year"
    val FILTER_ALL = "all"

}