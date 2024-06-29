package com.example.studyplannerapp.data.repositories

import android.app.Application
import com.example.studyplannerapp.data.local_db.TasksDao
import com.example.studyplannerapp.data.local_db.TasksDataBase
import com.example.studyplannerapp.data.models.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class TasksRepository(application: Application) {

    private var tasksDao:TasksDao?

    init {
        val database = TasksDataBase.getDatabase(application)
        tasksDao = database?.tasksDao()
    }

    fun getAllTasks() = tasksDao?.getAllTasks()

    fun getTask(id: Long) = tasksDao?.getTask(id)

    suspend fun addTask(task: Task) {
        tasksDao?.addTask(task)
    }

    suspend fun deleteTask(vararg task: Task) {
        tasksDao?.deleteTask(*task)
    }

    suspend fun updateTask(task: Task) {
        tasksDao?.updateTask(task)
    }
}