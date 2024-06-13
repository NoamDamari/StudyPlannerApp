package com.example.studyplannerapp.ui.all_tasks

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studyplannerapp.R
import com.example.studyplannerapp.data.models.Task
import com.example.studyplannerapp.data.models.TaskType
import java.util.Date

class TaskViewModel : ViewModel() {

    private val tasksLiveData : MutableLiveData<List<Task>> = MutableLiveData();

    init {
        tasksLiveData.value = mutableListOf(
            Task("Task 1" , "First Task", type = TaskType.PROJECT , course = "Android" , image = R.drawable.icon_assignment),
            Task("Android Assignment" , "Assignment android Course", type = TaskType.PROJECT , course = "Android" , image = R.drawable.icon_edit),
            Task("Android Project" , "Final project on android Course", type = TaskType.PROJECT , course = "Android" , image = R.drawable.icon_logout),
            Task("Task 4" , "Task Number 4"),
            Task("Task 5" , "Task Number 5"),
            Task("Task 6" , "Task Number 6"),
        )
    }

    fun getTasks() : MutableLiveData<List<Task>> {
        return tasksLiveData;
    }
}