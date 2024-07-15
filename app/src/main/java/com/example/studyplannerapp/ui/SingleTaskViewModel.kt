package com.example.studyplannerapp.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.studyplannerapp.data.models.Task
import com.example.studyplannerapp.data.repositories.TasksRepository

class SingleTaskViewModel: ViewModel(){
    private val repository = TasksRepository(application = Application())
    private val _id = MutableLiveData<Long>()
    private val _temporaryTask = MutableLiveData<Task>()
    val temporaryTask: MutableLiveData<Task> get() = _temporaryTask

    val task = _id.switchMap {
        repository.getTask(it)
    }
    fun setId(id: Long){
        _id.value = id
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
}