package com.scorelights.scorelights.data.rest.response

import com.google.gson.annotations.SerializedName
import com.scorelights.scorelights.data.domain.Clip

/**
 * Created by korji on 6/18/17.
 */
class ClipsResponse(
        @SerializedName("posts")
        var clips : List<Clip>,
        var after: String
)