package com.avjajodia.beacon.viewmodels

import android.app.Application
import android.app.admin.DevicePolicyManager
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.databinding.ObservableField
import android.os.RemoteException
import android.util.Log
import com.avjajodia.beacon.models.BeaconData
import com.avjajodia.beacon.receivers.AdminReceiver
import com.avjajodia.beacon.utilities.ResourceProviders
import org.altbeacon.beacon.*
import java.lang.Exception

class BeaconViewModel(application: Application) : AndroidViewModel(application), BeaconConsumer {

    private var beaconManager: BeaconManager
    private var beaconData: MutableLiveData<BeaconData>
    private val socket = ResourceProviders.getSocket()
    private var devicePolicyManager: DevicePolicyManager
    private var mAdmin: ComponentName
    private var cameraEnabled: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var dist: ObservableField<String>

    init {
        socket.connect()
        devicePolicyManager = applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mAdmin = ComponentName(applicationContext, AdminReceiver::class.java)
        beaconManager = BeaconManager.getInstanceForApplication(applicationContext)
        this.beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        this.beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"))

        beaconManager.bind(this)
        beaconData = MutableLiveData()
        listenServerData()


    }


    override fun getApplicationContext(): Context {
        return try {
            ResourceProviders.getApplicationContext()!!
        } catch (e: Exception) {
            e.printStackTrace()
            ResourceProviders.getApplicationContext()!!
        }
    }

    override fun unbindService(p0: ServiceConnection?) {
        beaconManager.unbind(this)
    }

    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int): Boolean {
        return true
    }

    override fun onBeaconServiceConnect() {

        beaconManager.addRangeNotifier { beacons, _ ->

            try {
                beacons.iterator().forEach {
                    beaconData.value = BeaconData(it.id1.toString(), it.distance)
                    dist = ObservableField(it.distance.toString())
                    emitDataToServer(it.id1.toString(), it.distance.toString())
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        try {
            beaconManager.startRangingBeaconsInRegion(Region("MyRegionId", null, null, null))
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

    fun getData(): LiveData<BeaconData> {
        return beaconData
    }

    private fun emitDataToServer(a: String, b: String) {
        socket.emit("new message", "$a => $b")
    }


    private fun listenServerData() {

        socket.on("new message") {
            if (it[0] == "DisableDeviceCamera") {
                Log.e("DeviceCam", "Disabled")
                devicePolicyManager.setCameraDisabled(mAdmin, true)
                cameraEnabled.postValue(false)
            } else if (it[0] == "EnableDeviceCamera") {
                Log.e("DeviceCam", "Enabled")
                devicePolicyManager.setCameraDisabled(mAdmin, false)
                cameraEnabled.postValue(true)
            }
        }
    }

    fun cameraStateChanged(): LiveData<Boolean> {
        return cameraEnabled
    }
}
