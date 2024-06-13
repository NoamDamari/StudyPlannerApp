package com.example.studyplannerapp.data.models

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Task(
    var title: String = "Task",
    var description: String = "",
    var deadline: Long = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR,1)
    }.timeInMillis,
    var type: TaskType = TaskType.OTHER,
    var course: String = "",
    var progressPercentage: Int = 0,
    var status: TaskStatus = TaskStatus.OPEN,
    //var image: String? = null)
    var image: Int? = null) {

    fun getFormattedDeadline() : String {
        val deadlineFormat = SimpleDateFormat("dd/MM/yy HH:mm" , Locale.getDefault())
        return deadlineFormat.format(this.deadline)
    }
}