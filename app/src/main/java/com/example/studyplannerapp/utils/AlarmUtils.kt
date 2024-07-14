package com.example.studyplannerapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.fragment.app.FragmentActivity
import com.example.studyplannerapp.data.models.Task
import com.example.studyplannerapp.data.receivers.TaskReminderReceiver
import com.google.android.material.snackbar.Snackbar

class AlarmUtils {

    companion object{

        /**
         * Sets an alarm notification for a task if permission is granted,
         * using AlarmManager and creating a pending intent.
         */
        fun setAlarmNotification(context: Context, task: Task){

            if (PermissionHandler.isPermissionGranted(context)) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = createAlarmIntent(context, task)
                val setAlarmIntent = createPendingIntent(context, task.id.toInt(), intent)
                val triggerTime = SystemClock.elapsedRealtime() + 5000L

                setExactAlarm(alarmManager, triggerTime, setAlarmIntent)
            } else {
                showPermissionSnackbar(context)
            }
        }

        /**
         * Creates an intent for a task reminder and passing task relevant data.
         */
        private fun createAlarmIntent(context: Context, task: Task): Intent {
            return Intent(context, TaskReminderReceiver::class.java).apply {
                putExtra("taskId", task.id.toInt())
                putExtra("taskName", task.title)
            }
        }

        /**
         * Creates a pending intent for use with an alarm
         */
        private fun createPendingIntent(context: Context, requestCode: Int, intent: Intent): PendingIntent {
            return PendingIntent.getBroadcast(
                context.applicationContext,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        /**
         * Sets an exact alarm using AlarmManager at a specified trigger time with a pending intent
         */
        private fun setExactAlarm(alarmManager: AlarmManager, triggerTime: Long, pendingIntent: PendingIntent) {
            alarmManager.setExact(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }

        /**
         * Cancels an alarm for a task
         */
        fun cancelAlarm(context: Context, taskId: Int) {
            val intent = Intent(context, TaskReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)

            if (context is FragmentActivity) {
                Snackbar.make(
                    context.findViewById(android.R.id.content),
                    "Notification Deleted",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        /**
         * Shows a Snack bar message indicating permissions were not granted for setting an alarm
         */
        private fun showPermissionSnackbar(context: Context) {
            if (context is FragmentActivity) {
                Snackbar.make(
                    context.findViewById(android.R.id.content),
                    "Permissions not granted to set alarm",
                    Snackbar.LENGTH_LONG
                ).setAction("Allow") {
                    PermissionHandler.requestNotificationPermission()
                }.show()
            }
        }
    }
}