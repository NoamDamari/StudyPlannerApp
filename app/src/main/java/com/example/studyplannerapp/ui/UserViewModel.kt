package com.example.studyplannerapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.studyplannerapp.data.models.User
import com.example.studyplannerapp.data.repositories.tasks.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)
    val user: LiveData<User?> = repository.getUser() ?: MutableLiveData()

    fun updateUser(newUser: User) {
        viewModelScope.launch {
            val currentUser = user.value
            if (currentUser == null) {
                repository.addUser(newUser)
            } else {
                repository.updateUser(newUser)
            }
        }
    }
}





