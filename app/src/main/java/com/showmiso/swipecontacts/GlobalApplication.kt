package com.showmiso.swipecontacts

import android.app.Application

class GlobalApplication : Application() {
    companion object {
        var instance: GlobalApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getGlobalApplicationContext(): GlobalApplication {
        checkNotNull(instance) {
            "the Instances is null"
        }
        return instance!!
    }
}