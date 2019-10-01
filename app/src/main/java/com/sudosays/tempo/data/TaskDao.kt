package com.sudosays.tempo.data

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

    @Query("SELECT * FROM tasks ORDER BY position ASC")
    fun getAll(): MutableList<Task>

    @Query("SELECT * FROM tasks WHERE uid == :uid")
    fun getTask(uid: Int): Task

    @Insert
    fun insertAll(vararg task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun delete(task: Task)


}