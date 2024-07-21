package com.example.studyplannerapp.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studyplannerapp.data.models.Task
import com.example.studyplannerapp.data.models.User

@Database(entities = [Task::class, User::class], version = 1, exportSchema = false)
abstract class TasksDataBase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao
    abstract fun usersDao(): UserDao
    companion object {

        @Volatile
        private var instance: TasksDataBase? = null

        fun getDatabase(context: Context) = instance ?: synchronized(this){
            Room.databaseBuilder(
                context.applicationContext,
                TasksDataBase::class.java,
                "tasks_database")
                .build().also { instance = it }
        }
    }
}