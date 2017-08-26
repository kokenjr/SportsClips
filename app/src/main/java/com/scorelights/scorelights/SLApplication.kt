package com.scorelights.scorelights

import com.scorelights.scorelights.BuildConfig
import com.scorelights.scorelights.di.AppComponent
import com.scorelights.scorelights.di.AppModule
import com.scorelights.scorelights.di.DaggerAppComponent
import io.fabric.sdk.android.Fabric
import timber.log.Timber

/**
 * Created by korji on 6/23/17.
 */
class SLApplication : android.app.Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        //Set logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            val fabric = Fabric.Builder(this)
                    .kits(com.crashlytics.android.Crashlytics())
                    .debuggable(true)
                    .build()
            Fabric.with(fabric)
        }

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}