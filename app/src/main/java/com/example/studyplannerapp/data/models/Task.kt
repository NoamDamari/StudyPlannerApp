package com.example.studyplannerapp.data.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Entity(tableName = "tasks_table")
data class Task(
    @PrimaryKey
    @ColumnInfo(name = "title")
    var title: String = "Task",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "deadline")
    var deadline: Long = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR,1)
    }.timeInMillis,
    @ColumnInfo(name = "type")
    var type: TaskType = TaskType.OTHER,
    @ColumnInfo(name = "course")
    var course: String = "",
    @ColumnInfo(name = "progress_percentage")
    var progressPercentage: Int = 0,
    @ColumnInfo(name = "status")
    var status: TaskStatus = TaskStatus.OPEN,
    @ColumnInfo(name = "image")
    //var image: String? = null)
    var image: Int? = null){
    //var image: Uri? = null){


    fun getFormattedDeadline() : String {
        val deadlineFormat = SimpleDateFormat("dd/MM/yy HH:mm" , Locale.getDefault())
        return deadlineFormat.format(this.deadline)
    }
}