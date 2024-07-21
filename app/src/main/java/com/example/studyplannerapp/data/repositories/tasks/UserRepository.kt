package com.example.studyplannerapp.data.repositories.tasks

import android.app.Application
import com.example.studyplannerapp.data.local_db.TasksDataBase
import com.example.studyplannerapp.data.local_db.UserDao
import com.example.studyplannerapp.data.models.User

class UserRepository(application: Application) {

    private var userDao : UserDao?

    init {
        val database = TasksDataBase.getDatabase(application)
        userDao = database.usersDao()
    }

    suspend fun addUser(user: User) {
        userDao?.addUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao?.deleteUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao?.updateUser(user)
    }

    fun getUser() = userDao?.getUserProfile()
}
