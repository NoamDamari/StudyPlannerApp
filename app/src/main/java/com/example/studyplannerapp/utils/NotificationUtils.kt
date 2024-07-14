package com.example.studyplannerapp.utils

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.studyplannerapp.R
import com.google.android.material.snackbar.Snackbar


class NotificationUtils() {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "2"
        private const val NOTIFICATION_CHANNEL_NAME = "Task Remainder"

        fun notify(
            context : Context,
            title : String,
            message : String,
            icon : Int = R.drawable.icon_assignment,
            id: Int
        ) {
            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(icon)

            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context)
                    .notify(id, notification.build())
            } else {
                // Handle the case where the permission is not granted
                Snackbar.make(
                    (context as Activity).findViewById(android.R.id.content),
                    "Notification permission not granted",
                    Snackbar.LENGTH_SHORT
                ).setAction("Allow") {
                    PermissionHandler.requestNotificationPermission()
                }
                    .show()
            }
        }

        fun createNotificationsChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )

                NotificationManagerCompat.from(context).createNotificationChannel(channel)
            }
        }
    }
}