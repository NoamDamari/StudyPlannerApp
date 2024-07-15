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

class CameraPermissionHandler {

    companion object {

        private lateinit var activity : FragmentActivity
        private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

        fun initialize(activity: FragmentActivity) {
            this.activity = activity
            requestPermissionLauncher =
                activity.registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        Snackbar.make(
                            activity.findViewById(android.R.id.content),
                            "Permission accepted",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(
                            activity.findViewById(android.R.id.content),
                            "Permission denied",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        fun checkCameraPermission() {
            if (isPermissionGranted(activity)) {
                handleCameraPermissionGranted()
            } else {
                handleCameraPermissionNotGranted()
            }
        }

        private fun handleCameraPermissionGranted() {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                "Camera permission already granted",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        private fun handleCameraPermissionNotGranted() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.CAMERA
                )
            ) {
                showRationaleAndRequestPermission()
            } else {
                requestCameraPermission()
            }
        }

        fun requestCameraPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        private fun showRationaleAndRequestPermission() {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                "Camera permission is required to take pictures.",
                Snackbar.LENGTH_LONG
            ).setAction("Allow") {
                requestCameraPermission()
            }.show()
        }

        private fun isPermissionGranted(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }

        private fun showSnackbar(message: String) {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}