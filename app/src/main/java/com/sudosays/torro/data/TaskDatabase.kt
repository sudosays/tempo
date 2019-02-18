package com.sudosays.torro.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 *
 * Author:
 *
 * Created on: 2019/02/18
 *
 */
@Database(entities = arrayOf(Task::class), version = 1)
abstract class TaskDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context): TaskDatabase =
                Room.databaseBuilder(context.applicationContext, TaskDatabase::class.java, "taskdb").build()
    }

    abstract fun taskDao(): TaskDao
}