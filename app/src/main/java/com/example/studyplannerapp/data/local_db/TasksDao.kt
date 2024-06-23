package com.example.studyplannerapp.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.studyplannerapp.data.models.Task

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(task: Task)

    @Delete
    fun deleteTask(vararg task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("SELECT * from tasks_table")
    fun getAllTasks() : LiveData<List<Task>>
}