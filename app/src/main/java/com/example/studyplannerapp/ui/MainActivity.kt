package com.example.studyplannerapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.studyplannerapp.R
import com.example.studyplannerapp.utils.PermissionHandler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionHandler.apply {
            initialize(this@MainActivity)
            checkNotificationPermission()
        }
    }
}