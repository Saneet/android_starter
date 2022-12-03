package com.saneet.demo.dagger

import android.app.Application
import com.saneet.demo.DemoApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DemoAppModule constructor(private val application: DemoApplication) {
    @Provides
    @Singleton
    fun getApplication(): Application {
        return application
    }
}