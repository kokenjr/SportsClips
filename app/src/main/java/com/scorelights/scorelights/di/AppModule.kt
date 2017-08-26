package com.scorelights.scorelights.di

import android.content.Context
import com.scorelights.scorelights.SLApplication
import com.scorelights.scorelights.data.rest.RestApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by korji on 6/25/17.
 */
@Module
class AppModule(val slApplication: SLApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return slApplication
    }

    @Provides
    @Singleton
    fun provideApplication(): SLApplication = slApplication

    @Provides
    @Singleton
    fun provideRestApi(): RestApi {
        return RestApi(slApplication)
    }
}
