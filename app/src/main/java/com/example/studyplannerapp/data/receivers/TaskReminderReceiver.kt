package com.example.studyplannerapp.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.studyplannerapp.utils.NotificationUtils

class TaskReminderReceiver : BroadcastReceiver() {

    /**
     * BroadcastReceiver to handle task reminder notifications
     * Receives broadcast intents with task details and triggers notifications
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        // Retrieves task ID and name from the received intent
        val taskId = intent?.getIntExtra("taskId", -1)
        val taskName = intent?.getStringExtra("taskName")

        // Notify the user about the upcoming task submission
        if (taskId != null) {
            NotificationUtils.notify(context!!, "Submission Alert" , "Upcoming submission: $taskName", id=taskId)
        }
    }
}