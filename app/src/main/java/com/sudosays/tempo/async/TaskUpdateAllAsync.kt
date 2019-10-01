package com.sudosays.tempo.async

import android.os.AsyncTask
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskDatabase

/**
 * Created by Julius on 2019/10/01.
 */
class TaskUpdateAllAsync(val db: TaskDatabase): AsyncTask<Task, Void, Boolean>(){

    override fun doInBackground(vararg p0: Task?): Boolean {

        var success = false

        for (t in p0) {
            t?.let {
                db.taskDao().updateTask(t)
                success =true
            }

            if (!success) {
                break
            }
        }

        return success
    }
}