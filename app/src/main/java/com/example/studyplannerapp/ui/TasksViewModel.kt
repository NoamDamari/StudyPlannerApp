package com.example.studyplannerapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studyplannerapp.data.models.Task
import com.example.studyplannerapp.data.repositories.TasksRepository

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TasksRepository(application)
    val tasksLiveData : LiveData<List<Task>>? = repository.getAllTasks()

    private val _selectedTask = MutableLiveData<Task>()
    val selectedTask: LiveData<Task> get() = _selectedTask

    fun addTask(task: Task) {
        repository.addTask(task)
    }

    fun deleteTask(task: Task) {
        repository.deleteTask(task)
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

}