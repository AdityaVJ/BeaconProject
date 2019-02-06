package com.avjajodia.beacon.utilities

import android.content.Context
import android.preference.PreferenceManager
import java.lang.Exception
import java.lang.NullPointerException


/**
 * Created by Aditya V Jajodia on 03-07-2018.
 */

class Preferences {

    companion object {

        fun putPref(key: String, value: String, context: Context) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun putPref(key: String, value: Boolean, context: Context) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> getPref(key: String, context: Context): T? {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)

            return try {
                preferences.all.getValue(key) as T
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

}