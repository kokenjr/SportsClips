package com.scorelights.scorelights.di

import com.scorelights.scorelights.ui.home.ClipsFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by korji on 6/26/17.
 */
@Singleton
@Component(modules = arrayOf(
        AppModule::class)
)
interface AppComponent {

    fun inject(clipsFragment: ClipsFragment)

}
