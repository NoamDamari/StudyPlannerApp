package com.example.studyplannerapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

class PermissionHandler() {

    companion object {

        private lateinit var activity : FragmentActivity
        private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

        /**
         * Manages initialization of activity and permission launcher.
         */
        fun initialize(activity: FragmentActivity){
            this.activity = activity
            requestPermissionLauncher =
                Companion.activity.registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    when {
                        isGranted -> {
                            Snackbar.make(
                                Companion.activity.findViewById(android.R.id.content),
                                "Permission accepted",
                                Snackbar.LENGTH_SHORT
                            ).show()

                            NotificationUtils.createNotificationsChannel(Companion.activity)
                        }
                    }
                }
        }

        /**
         * Checks if notification permission is granted and handles accordingly.
         */
        fun checkNotificationPermission() {
            if (isPermissionGranted(activity)) {
                handlePermissionGranted()
            } else {
                handlePermissionNotGranted()
            }
        }

        /**
         * Handles actions when notification permission is already granted.
         */
        private fun handlePermissionGranted() {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                "Notification permission already granted",
                Snackbar.LENGTH_SHORT
            ).show()

            // Create notification channel
            NotificationUtils.createNotificationsChannel(activity)
        }

        /**
         * Handles actions when notification permission is not granted.
         * Shows rationale and requests permission if necessary.
         */
        private fun handlePermissionNotGranted() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)) {
                showRationaleAndRequestPermission()
            } else {
                requestNotificationPermission()
            }
        }

        /**
         * Shows rationale for notification permission and requests it.
         */
        private fun showRationaleAndRequestPermission() {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                "Notification permission is required to send you reminders.",
                Snackbar.LENGTH_LONG
            ).setAction("Allow") {
                requestNotificationPermission()
            }.show()
        }

        /**
         * Requests notification permission using the launcher
         */
        fun requestNotificationPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        /**
         * Checks if notification permission is granted
         */
        fun isPermissionGranted(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}