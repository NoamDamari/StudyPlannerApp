package com.example.studyplannerapp.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.studyplannerapp.data.models.Task
import com.example.studyplannerapp.data.repositories.tasks.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleTaskViewModel: ViewModel(){

    private val repository = TasksRepository(application = Application())
    private val _id = MutableLiveData<Long>()

    val editedTask = MutableLiveData<Task>()

    val selectedTask: LiveData<Task> = _id.switchMap { id ->
        repository.getTask(id)
    }

    private val mediator = MediatorLiveData<Task>().apply {
        addSource(selectedTask) { task ->
            value = task?.copy()
        }
    }

    init {
        mediator.observeForever { task ->
            editedTask.value = task
        }
    }

    fun setId(id: Long){
        _id.value = id
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun updateTemporaryTaskFromUI(
        title: String,
        description: String,
        deadline: Long,
        type: String,
        course: String,
        progressPercentage: Int,
        image: String?
    ) {
        val updatedTask = editedTask.value ?: Task()
        updatedTask.title = title
        updatedTask.description = description
        updatedTask.deadline = deadline
        updatedTask.type = type
        updatedTask.course = course
        updatedTask.progressPercentage = progressPercentage
        updatedTask.image = image ?: updatedTask.image
        editedTask.value = updatedTask
    }
}