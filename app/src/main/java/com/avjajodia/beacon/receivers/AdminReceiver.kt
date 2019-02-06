package com.avjajodia.beacon.receivers

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import com.avjajodia.beacon.R
import com.avjajodia.beacon.utilities.Preferences
import java.lang.NullPointerException


/**
 * Created by Aditya V Jajodia on 06-02-2019.
 */
class AdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context?, intent: Intent?) {
        super.onEnabled(context, intent)

        try {
            Preferences.putPref(context!!.getString(R.string.device_admin_pref), true, context)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun onDisabled(context: Context?, intent: Intent?) {
        super.onDisabled(context, intent)

        try {
            Preferences.putPref(context!!.getString(R.string.device_admin_pref), false, context)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }
}