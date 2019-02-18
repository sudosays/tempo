package com.sudosays.torro.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 *
 * Author:
 *
 * Created on: 2019/02/18
 *
 */
@Database(entities = arrayOf(Task::class), version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}