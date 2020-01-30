package com.jmz.sleepwellgsensor

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.WindowManager

internal class ScreenRotator(context: Context) {

    private val layoutParams = WindowManager.LayoutParams()
    private val view = View(context)
    private val winMan = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var viewAdded = false

    init {

        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        layoutParams.width = 0
        layoutParams.height = 0
        layoutParams.format = PixelFormat.TRANSPARENT
        layoutParams.alpha = 0f
        layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        winMan.addView(view, layoutParams)
        viewAdded = true

        view.visibility = View.VISIBLE
    }

    fun DoRotateScreen(desiredOrientation: Int) {

        /*when(layoutParams.screenOrientation) {

            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> {

                layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> {

                layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            }
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT -> {

                layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            }
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE -> {

                layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }*/

        layoutParams.screenOrientation = desiredOrientation

        winMan.updateViewLayout(view, layoutParams)
    }

    fun Finalize() {

        if(viewAdded)
            winMan.removeView(view)
    }
}