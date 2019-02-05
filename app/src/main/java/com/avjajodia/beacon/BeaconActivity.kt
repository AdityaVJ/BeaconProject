package com.avjajodia.beacon

import android.Manifest
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
import android.bluetooth.BluetoothAdapter
import com.avjajodia.beacon.fragments.BeaconFragment


class BeaconActivity : AppCompatActivity() {

    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon)

        if (!mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.enable()
        }

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, BeaconFragment()).commit()

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
}
