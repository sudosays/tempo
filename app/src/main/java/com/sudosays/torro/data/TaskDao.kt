package com.sudosays.torro.data

import android.arch.persistence.room.*

/**
 *
 * Author:
 *
 * Created on: 2019/02/18
 *
 */
@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAll(): MutableList<Task>

    @Insert
    fun insertAll(vararg task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun delete(task: Task)


}