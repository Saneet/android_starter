package com.saneet.demo

import android.app.Application
import android.os.StrictMode
import com.saneet.demo.dagger.AppComponent
import com.saneet.demo.dagger.DaggerAppComponent
import com.saneet.demo.dagger.DemoAppModule

class DemoApplication : Application() {

    public lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        initDemoAppComponent()
        setupStrictMode()
    }

    private fun setupStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
        super.onCreate()
    }

    private fun initDemoAppComponent() {
        appComponent = DaggerAppComponent
            .builder()
            .appModule(DemoAppModule(this))
            .build()
    }
}