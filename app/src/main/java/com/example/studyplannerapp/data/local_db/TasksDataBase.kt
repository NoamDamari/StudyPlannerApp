package com.example.studyplannerapp.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studyplannerapp.data.models.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TasksDataBase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao

    companion object {

        @Volatile
        private var instance: TasksDataBase? = null

        //TODO: remove allowMainThreadQueries
        fun getDatabase(context: Context) = instance ?: synchronized(this){
            Room.databaseBuilder(
                context.applicationContext,
                TasksDataBase::class.java,
                "tasks_database")
                .allowMainThreadQueries()
                .build().also { instance = it }
        }
    }
}