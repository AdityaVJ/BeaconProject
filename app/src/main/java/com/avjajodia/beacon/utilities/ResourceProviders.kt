package com.avjajodia.beacon.utilities

import android.content.Context
import com.avjajodia.beacon.application.BeaconApplication
import io.socket.client.Socket


/**
 * Created by Aditya V Jajodia on 05-02-2019.
 */
class ResourceProviders {

    companion object {

        fun getApplicationContext(): Context? {
            return BeaconApplication.instance?.applicationContext
        }


        fun getSocket(): Socket {
            return BeaconApplication.getSocket()
        }
    }

}