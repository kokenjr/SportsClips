package com.scorelights.scorelights.data.domain

import com.google.gson.annotations.SerializedName
import com.scorelights.scorelights.ui.adapter.utils.ViewType
import com.scorelights.scorelights.common.Constants
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

/**
 * Created by korji on 5/16/17.
 */
@PaperParcel
class Clip(
        val id: String,
        val title: String,
        val url: String,
        val link: String,
        val score: Int,
        val thumbnail: String,
        val width: Int,
        val height: Int,
        val created: Long,
        val author: String,
        @SerializedName("share_url")
        val shareUrl : String,
        var muted: Boolean,
        var seekPosition: Long,
        var clipIndex: Int,
        var playing: Boolean
) : PaperParcelable, ViewType {
    override fun getViewType() = Constants.CLIPS
    companion object {
        @JvmField val CREATOR = PaperParcelClip.CREATOR
    }
}
