package com.example.studyplannerapp.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.studyplannerapp.data.repositories.TasksRepository

class SingleTaskViewModel: ViewModel(){
    private val repository = TasksRepository(application = Application())
    private val _id = MutableLiveData<Long>()

    val task = _id.switchMap {
        repository.getTask(it)
    }
    fun setId(id: Long){
        _id.value = id
    }
}