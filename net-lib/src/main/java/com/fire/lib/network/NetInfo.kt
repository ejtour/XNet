package com.fire.lib.network

import android.app.Application

interface NetInfo {

    fun showLog(): Boolean

    fun getAppVersionName(): String?

    fun getAppVersionCode(): String?

    fun getApplicationContext(): Application?
}