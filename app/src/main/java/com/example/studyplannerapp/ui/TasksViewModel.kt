package com.example.studyplannerapp.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.studyplannerapp.data.models.Task
import com.example.studyplannerapp.data.models.User
import com.example.studyplannerapp.data.repositories.tasks.TasksRepository
import com.example.studyplannerapp.data.repositories.tasks.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TasksRepository(application)
    private val userRepository = UserRepository(application)

    val tasksLiveData : LiveData<List<Task>>? = repository.getAllTasks()

    val userLiveData: LiveData<User?>? = userRepository.getUser()

    private val _temporaryTask = MutableLiveData<Task>()
    val temporaryTask: LiveData<Task> get() = _temporaryTask

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun setTempTaskDescription(description: String) {
        val tempTask = _temporaryTask.value ?: Task()
        tempTask.description = description
        _temporaryTask.value = tempTask
    }
    fun setTempTaskImage(imageUri: Uri?) {
        val tempTask = _temporaryTask.value ?: Task()
        tempTask.image = imageUri?.toString()
        _temporaryTask.value = tempTask
    }

    fun updateTemporaryTaskDescriptionFromUI(
        description: String,
    ) {
        val updatedTask = _temporaryTask.value ?: Task()
        updatedTask.description = description
        _temporaryTask.value = updatedTask
    }

    fun setTempTaskDate(date: Long) {
        val tempTask = _temporaryTask.value ?: Task()
        tempTask.deadline = date
        _temporaryTask.value = tempTask
    }

    fun resetTemporaryTask() {
        val currentDate = _temporaryTask.value?.deadline ?: System.currentTimeMillis()
        _temporaryTask.value = Task().apply {
            deadline = currentDate
        }
    }
}