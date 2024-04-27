package com.fire.lib.utils

import android.os.Build
import android.util.Log

class LogUtils {

    companion object {
        fun d(tag: String = "", msg: String = "") {
            Log.d(tag, msg)
        }
    }
}