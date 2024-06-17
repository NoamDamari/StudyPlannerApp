package com.example.studyplannerapp.data.repositories

import android.app.Application
import com.example.studyplannerapp.data.local_db.TasksDao
import com.example.studyplannerapp.data.local_db.TasksDataBase
import com.example.studyplannerapp.data.models.Task

class TasksRepository(application: Application) {

    private var tasksDao:TasksDao?

    init {
        val database = TasksDataBase.getDatabase(application)
        tasksDao = database?.tasksDao()
    }

    fun getAllTasks() = tasksDao?.getAllTasks()

    fun addTask(task: Task) {
        tasksDao?.addTask(task)
    }

    fun deleteTask(vararg task: Task) {
        tasksDao?.deleteTask(*task)
    }

    fun updateTask(task: Task) {
        tasksDao?.updateTask(task)
    }
}