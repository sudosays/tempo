package com.sudosays.tempo.async

import android.os.AsyncTask
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskDatabase

/**
 *
 * Author: Julius Stopforth
 *
 * Created on: 2019/02/18
 *
 */
class TaskDeleteAsync(val db: TaskDatabase): AsyncTask<Task, Void, Boolean> (){

    override fun doInBackground(vararg p0: Task?): Boolean {
        p0[0]?.let {
            db.taskDao().delete(it)
            return true
        }
        return false
    }
}