package com.avjajodia.beacon.fragments

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.databinding.ObservableField
import android.os.RemoteException
import android.util.Log
import com.avjajodia.beacon.models.BeaconData
import com.avjajodia.beacon.utilities.ResourceProviders
import org.altbeacon.beacon.*
import java.lang.Exception

class BeaconViewModel : ViewModel(), BeaconConsumer {

    private var beaconManager: BeaconManager
    private lateinit var beaconData: MutableLiveData<BeaconData>

    lateinit var dist: ObservableField<String>

    init {
        beaconManager = BeaconManager.getInstanceForApplication(applicationContext)
        this.beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        this.beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"))
        beaconManager.bind(this)
        beaconData = MutableLiveData()
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

        beaconManager.addRangeNotifier { beacons, region ->

            try {

                beacons.iterator().forEach {
                    beaconData.value = BeaconData(it.id1.toString(), it.distance)
                    dist = ObservableField(it.distance.toString())
                    //dist.set(it.distance.toString())
                    /*Log.e("id1", it.id1.toString())
                    Log.e("id2", it.id2.toString())
                    Log.e("id3", it.id3.toString())
                    Log.e("Distance", it.distance.toString())
                    Log.e("UniqueID", region.uniqueId)*/
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


}
