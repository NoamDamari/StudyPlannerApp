package com.example.studyplannerapp.data.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studyplannerapp.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Entity(tableName = "tasks_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "title")
    var title: String = "Task",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "deadline")
    var deadline: Long = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR,1)
    }.timeInMillis,
    @ColumnInfo(name = "type")
    var type: String = "",
    @ColumnInfo(name = "course")
    var course: String = "",
    @ColumnInfo(name = "progress_percentage")
    var progressPercentage: Int = 0,
    @ColumnInfo(name = "image")
    var image: String? = R.drawable.icon_assignment.toString()){

    fun getFormattedDeadline() : String {
        val deadlineFormat = SimpleDateFormat("MMM dd, yyyy" , Locale.getDefault())
        return deadlineFormat.format(this.deadline)
    }
}