package com.scorelights.scorelights.ui.adapter.delegate

import android.support.v7.widget.RecyclerView
import com.scorelights.scorelights.ui.adapter.utils.ViewType

/**
 * Created by korji on 5/16/17.
 */
interface ViewTypeDelegateAdapter {

    fun onCreateViewHolder(parent: android.view.ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int)
    fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder, item: ViewType, position: Int)
}