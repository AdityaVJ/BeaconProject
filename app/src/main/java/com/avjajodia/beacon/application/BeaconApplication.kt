package com.avjajodia.beacon.application

import android.app.Application
import com.avjajodia.beacon.constants.Constants
import com.facebook.stetho.Stetho
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException


/**
 * Created by Aditya V Jajodia on 05-02-2019.
 */
class BeaconApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this

        Stetho.initializeWithDefaults(this)

        try {
            mSocket = IO.socket(Constants.SERVER_URL)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

    }

    companion object {
        @get:Synchronized
        var instance: BeaconApplication? = null
            private set

        private lateinit var mSocket: Socket

        fun getSocket(): Socket {
            return mSocket
        }
    }

}