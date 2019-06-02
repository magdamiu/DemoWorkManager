package com.magdamiu.demoworkmanager

import android.util.Log

object Logging {
    fun show(obj: Any, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(obj.javaClass.name, message)
        }
    }
}