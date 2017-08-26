package com.scorelights.scorelights

import org.mockito.Mockito

/**
 * Created by korji on 5/16/17.
 */
inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)
