package com.avjajodia.beacon.utilities

import android.content.Context
import com.avjajodia.beacon.application.BeaconApplication


/**
 * Created by Aditya V Jajodia on 05-02-2019.
 */
class ResourceProviders {

    companion object {

        fun getApplicationContext(): Context? {
            return BeaconApplication.instance?.applicationContext
        }

    }

}