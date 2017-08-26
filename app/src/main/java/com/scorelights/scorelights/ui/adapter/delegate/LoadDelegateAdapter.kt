package com.scorelights.scorelights.ui.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.scorelights.scorelights.R
import com.scorelights.scorelights.ui.adapter.utils.ViewType
import com.scorelights.scorelights.ui.adapter.utils.inflate

/**
 * Created by korji on 5/16/17.
 */
class LoadDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup) = LoadDelegateAdapter.TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
    }

    class TurnsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.view_loading))
}