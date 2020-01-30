package com.jmz.sleepwellgsensor

import android.util.Log

internal class Logger {

    companion object {

        private val NAME_APP = "SleepWellGSensor"

        fun LogInfo(info: String) { Log.i(NAME_APP, info) }
        fun LogWarning(warning: String) { Log.w(NAME_APP, warning) }
        fun LogError(error: String) { Log.e(NAME_APP, error) }
    }
}