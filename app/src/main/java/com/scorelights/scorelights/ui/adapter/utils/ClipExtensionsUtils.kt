package com.scorelights.scorelights.ui.adapter.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by korji on 6/14/17.
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}