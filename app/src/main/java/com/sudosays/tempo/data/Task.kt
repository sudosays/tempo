package com.sudosays.tempo.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 *
 * Author:
 *
 * Created on: 2018/06/02
 *
 */

@Entity (tableName = "tasks")
data class Task(@PrimaryKey(autoGenerate = true) var uid :Int,
                @ColumnInfo(name = "task_name") var name:String,
                @ColumnInfo(name = "task_duration") var duration:Float)
