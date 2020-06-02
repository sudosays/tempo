package com.sudosays.tempo.async

import android.os.AsyncTask
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskDatabase

/**
 * Created by Julius on 2019/07/05.
 */
class TaskUpdateAsync(val db: TaskDatabase) : AsyncTask<Task, Void, Boolean>() {

    override fun doInBackground(vararg p0: Task?): Boolean {
        p0[0]?.let {
            db.taskDao().updateTask(it)
            return true
        }
        return false
    }
}