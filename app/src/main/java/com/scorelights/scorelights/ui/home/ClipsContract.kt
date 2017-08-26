package com.scorelights.scorelights.ui.home

import com.scorelights.scorelights.data.domain.Clip

/**
 * Created by korji on 6/26/17.
 */
interface ClipsContract {
    interface View {
        fun setClips(after: String?, clips: List<Clip>)
        fun showError()
    }

    interface Presenter {

        fun takeView(clipsView: ClipsContract.View)

        fun dropView()

        fun requestClips(cacheControl: String, after: String?, sub: String, time: String, sort: String)
    }
}
