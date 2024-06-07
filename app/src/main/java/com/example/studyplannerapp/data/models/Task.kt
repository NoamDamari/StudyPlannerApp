package com.example.studyplannerapp.data.models

import java.util.Calendar

data class Task(
    var title: String = "Task",
    var description: String = "",
    var deadline: Long = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR,1)
    }.timeInMillis,
    var type: TaskType = TaskType.OTHER,
    var course: String = "",
    var status: TaskStatus = TaskStatus.OPEN,
    var image: String? = null) {
}