package com.avjajodia.beacon

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import org.altbeacon.beacon.*

import java.util.ArrayList
import java.lang.Compiler.disable
import android.bluetooth.BluetoothAdapter


class BeaconActivity : AppCompatActivity(), BeaconConsumer {
    private var beaconList: ArrayList<String>? = null
    private var beaconListView: ListView? = null
    private var adapter: ArrayAdapter<String>? = null
    private var beaconManager: BeaconManager? = null
    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon)

        if (!mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.enable()
        }

        this.beaconList = ArrayList()
        this.beaconListView = findViewById(R.id.listView)
        this.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.beaconList!!)
        this.beaconListView!!.adapter = adapter
        this.beaconManager = BeaconManager.getInstanceForApplication(this)
        this.beaconManager!!.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        this.beaconManager!!.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"))
        this.beaconManager!!.bind(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("This app needs location access")
                builder.setMessage("Please grant location access so this app can detect beacons")
                builder.setPositiveButton(android.R.string.ok, null)
                builder.setOnDismissListener {
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        1
                    )
                }
                builder.show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.beaconManager!!.unbind(this)
    }

    override fun onBeaconServiceConnect() {
        this.beaconManager!!.setRangeNotifier { beacons, region ->
            if (beacons.size > 0) {
                beaconList!!.clear()
                val iterator = beacons.iterator()
                while (iterator.hasNext()) {
                    beaconList!!.add(iterator.next().id1.toString())
                }
                runOnUiThread { adapter!!.notifyDataSetChanged() }
            }
        }
        try {
            this.beaconManager!!.startRangingBeaconsInRegion(Region("MyRegionId", null, null, null))
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

    companion object {

        private val TAG = "BEACON_PROJECT"
    }


}
