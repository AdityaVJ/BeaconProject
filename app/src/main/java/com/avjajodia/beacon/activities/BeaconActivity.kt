package com.avjajodia.beacon.activities

import android.Manifest
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.avjajodia.beacon.R
import com.avjajodia.beacon.fragments.BeaconFragment
import com.avjajodia.beacon.receivers.AdminReceiver


class BeaconActivity : AppCompatActivity() {

    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private val permissionRequest: Int = 1
    private val activationRequest: Int = 2
    private lateinit var mAdmin: ComponentName

    private interface ActionResult {
        fun onResult(permissionGranted: Boolean, requestNumber: Int)
    }

    private lateinit var actionResult: ActionResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon)

        val devicePolicyManager: DevicePolicyManager? =
            getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        checkAndRequestPermissions()
        mAdmin = ComponentName(this, AdminReceiver::class.java)

        if (devicePolicyManager != null && !devicePolicyManager.isAdminActive(mAdmin)) {

            val deviceAdminPromptIntent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            deviceAdminPromptIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdmin)
            deviceAdminPromptIntent.putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.admin_description)
            )

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Admin Access Required")
                .setMessage("Please make ${getString(R.string.app_name)} Device Admin to continue.")
                .setCancelable(false)
                .setPositiveButton("Settings") { _, _ ->
                    // Do nothing here
                }
                .create()

            alertDialog.setOnShowListener {

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    startActivityForResult(deviceAdminPromptIntent, activationRequest)

                    actionResult = object : ActionResult {
                        override fun onResult(permissionGranted: Boolean, requestNumber: Int) {

                            if (requestNumber == activationRequest) {

                                if (permissionGranted)
                                    alertDialog.dismiss()
                                else
                                    Toast.makeText(
                                        applicationContext,
                                        "This permission is mandatory to continue!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }

                        }
                    }
                }

            }

            alertDialog.show()

        }

        if (!mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.enable()
        }

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, BeaconFragment()).commit()
    }

    private fun checkAndRequestPermissions() {

        val locationAccessPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val listPermissionsNeeded = mutableListOf<String>()

        if (locationAccessPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (!listPermissionsNeeded.isEmpty()) {

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("Please grant Location Access to continue.")
                .setCancelable(false)
                .setPositiveButton("Request") { _, _ ->
                    // Do nothing here
                }
                .create()

            alertDialog.setOnShowListener {

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                    ActivityCompat.requestPermissions(
                        this,
                        listPermissionsNeeded.toTypedArray(),
                        permissionRequest
                    )

                    actionResult = object : ActionResult {
                        override fun onResult(permissionGranted: Boolean, requestNumber: Int) {
                            if (permissionGranted && requestNumber == permissionRequest)
                                alertDialog.dismiss()
                        }
                    }
                }

            }

            alertDialog.show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            activationRequest -> {

                if (resultCode == Activity.RESULT_OK)
                    actionResult.onResult(true, activationRequest)
                else
                    actionResult.onResult(false, activationRequest)

            }

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            permissionRequest -> {

                var requestPermissionsResult = true

                for (i in grantResults) {
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        requestPermissionsResult = false
                        break
                    }
                }

                actionResult.onResult(requestPermissionsResult, permissionRequest)
            }

        }

    }
}
