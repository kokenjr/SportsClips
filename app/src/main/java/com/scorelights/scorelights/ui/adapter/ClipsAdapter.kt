package com.scorelights.scorelights.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.scorelights.scorelights.ui.adapter.delegate.ClipsDelegateAdapter
import com.scorelights.scorelights.ui.adapter.delegate.LoadDelegateAdapter
import com.scorelights.scorelights.ui.adapter.delegate.ViewTypeDelegateAdapter
import com.scorelights.scorelights.ui.adapter.utils.ViewType
import com.scorelights.scorelights.common.Constants
import com.scorelights.scorelights.data.domain.Clip

/**
 * Created by korji on 5/16/17.
 */
class ClipsAdapter(fragment: Fragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var clips: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType {
        override fun getViewType() = Constants.LOADING
    }
    private val mFragment: Fragment

    init {
        delegateAdapters.put(Constants.LOADING, LoadDelegateAdapter())
        delegateAdapters.put(Constants.CLIPS, ClipsDelegateAdapter(fragment))
        clips = ArrayList()
        clips.add(loadingItem)
        mFragment = fragment
    }

    override fun getItemCount(): Int {
        return clips.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, this.clips[position], position)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewAttachedToWindow(holder)

        val position = holder!!.adapterPosition

        if (position >= 0) {
            delegateAdapters.get(getItemViewType(position)).onViewAttachedToWindow(holder, this.clips[position], position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): android.support.v7.widget.RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun getItemViewType(position: Int): Int {
        if (this.clips.size == 0){
            clips.add(loadingItem)
        }
        return this.clips[position].getViewType()
    }

    fun addClips(clips: List<Clip>) {
        val initPosition = this.clips.size - 1
        removeClip(initPosition)

        if (clips.isNotEmpty()) {
            this.clips.addAll(clips)
            this.clips.add(loadingItem)
            notifyItemRangeChanged(initPosition, this.clips.size + 1 /* plus loading item */)
        }
    }

    private fun removeClip(initPosition: Int) {
        this.clips.removeAt(initPosition)
        notifyItemRemoved(initPosition)
    }

    fun clear(){
        this.clips.clear()
        notifyDataSetChanged()
        clips.add(loadingItem)
    }

    fun removeLoading() {
        if (clips.size > 0) {
            val clipIndex = clips.size - 1
            val clip = clips[clipIndex]
            if (clip.getViewType() == Constants.LOADING) {
                removeClip(clipIndex)
            }
        }
    }
}