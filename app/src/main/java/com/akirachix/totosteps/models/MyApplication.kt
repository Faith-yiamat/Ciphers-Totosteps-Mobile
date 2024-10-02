package com.akirachix.totosteps.models

import android.app.Application
import com.akirachix.totosteps.api.ApiClient

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ApiClient.initialize(this)
    }
}