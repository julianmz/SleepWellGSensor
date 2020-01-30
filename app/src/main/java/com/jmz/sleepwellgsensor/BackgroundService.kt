package com.jmz.sleepwellgsensor

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

internal class BackgroundService : Service() {

    private inner class CmdReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            if(intent != null) {

                val actionName = intent.action
                if(actionName != null && actionName.equals(NAME_ACTION_ROTATE_LANDSCAPE))
                    OnRotate(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                else if(actionName != null && actionName.equals(NAME_ACTION_ROTATE_PORTRAIT))
                    OnRotate(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                if(actionName != null && actionName.equals(NAME_ACTION_ROTATE_LANDSCAPE_REVERSE))
                    OnRotate(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
            }
        }
    }

    companion object {

        private val NAME_ACTION_ROTATE_LANDSCAPE = "Landscape"
        private val NAME_ACTION_ROTATE_PORTRAIT = "Portrait"
        private val NAME_ACTION_ROTATE_LANDSCAPE_REVERSE = "RLandscape"
    }

    private val cmdReceiver = CmdReceiver()
    private var screenRotator: ScreenRotator? = null

    override fun onBind(intent: Intent?): IBinder? {

        return null;
    }

    override fun onCreate() {

        Logger.LogInfo("Creating background service...")

        super.onCreate()

        Logger.LogInfo("Background service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Logger.LogInfo("Starting background service...")

        super.onStartCommand(intent, flags, startId)

        screenRotator = ScreenRotator(this)

        val intentFilter = IntentFilter()
        intentFilter.addAction(NAME_ACTION_ROTATE_LANDSCAPE)
        intentFilter.addAction(NAME_ACTION_ROTATE_PORTRAIT)
        intentFilter.addAction(NAME_ACTION_ROTATE_LANDSCAPE_REVERSE)

        registerReceiver(cmdReceiver, intentFilter)

        val notification = NotificationCompat
            .Builder(this, CreateNotificationChannel())
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setContentTitle("SleepWellGSensor")
            .setContentText("Screen rotation service")
            .addAction(R.drawable.ic_rotate_logo, NAME_ACTION_ROTATE_LANDSCAPE,
                CreateRotationPendingIntent(NAME_ACTION_ROTATE_LANDSCAPE, 667))
            .addAction(R.drawable.ic_rotate_logo, NAME_ACTION_ROTATE_PORTRAIT,
                CreateRotationPendingIntent(NAME_ACTION_ROTATE_PORTRAIT, 668))
            .addAction(R.drawable.ic_rotate_logo, NAME_ACTION_ROTATE_LANDSCAPE_REVERSE,
                CreateRotationPendingIntent(NAME_ACTION_ROTATE_LANDSCAPE_REVERSE, 669))
            .build()

        startForeground(666, notification)

        Logger.LogInfo("Background service started")

        return START_NOT_STICKY;
    }

    override fun onDestroy() {

        Logger.LogInfo("Destroying background service...")

        super.onDestroy()

        unregisterReceiver(cmdReceiver);

        screenRotator!!.Finalize()

        Logger.LogInfo("Background service destroyed")
    }

    private fun CreateNotificationChannel() : String {

        var notificationChannelId = "default"

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationChannelId = "com.jmz.sleepwellgsensor.BackgroundService.NotificationChannel"

            val notificationChannelName = "SleepWellGSensor Background Service"
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                notificationChannelName,
                NotificationManager.IMPORTANCE_DEFAULT)

            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        return notificationChannelId
    }

    private fun CreateRotationPendingIntent(name: String, requestCode: Int) : PendingIntent {

        val rotateIntent = Intent()
        rotateIntent.setAction(name)

        return PendingIntent.getBroadcast(this,
            requestCode,
            rotateIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun OnRotate(desiredOrientation: Int) {

        Logger.LogInfo("Rotating screen...")

        screenRotator!!.DoRotateScreen(desiredOrientation)

        Logger.LogInfo("Screen rotated")
    }

    private fun OnQuit() {

        Logger.LogInfo("Quitting background service")

        stopSelf()
    }
}