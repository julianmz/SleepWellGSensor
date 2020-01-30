package com.jmz.sleepwellgsensor

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings

class MainActivity : Activity() {

    private var backgroundService: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.LogInfo("SleepWellGSensor launched")

        if(!Settings.canDrawOverlays(this)) {

            Logger.LogWarning("Requesting overlay permission")

            val requestPermissionIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            requestPermissionIntent.setData(Uri.parse("package:" + packageName))

            startActivity(requestPermissionIntent)
        }
        else {

            if (backgroundService == null) {

                backgroundService = Intent(this, BackgroundService::class.java)
                startService(backgroundService)
            }
        }

        finish()
    }
}