package com.avjajodia.beacon.application

import android.app.Application


/**
 * Created by Aditya V Jajodia on 05-02-2019.
 */
class BeaconApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        @get:Synchronized
        var instance: BeaconApplication? = null
            private set
    }

}