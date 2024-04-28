package com.fire.lib.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fire.lib.network.R

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
    }

    abstract fun layoutId(): Int
}