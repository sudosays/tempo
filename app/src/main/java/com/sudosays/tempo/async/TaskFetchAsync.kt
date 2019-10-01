package com.sudosays.tempo.async

import android.os.AsyncTask
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskDatabase

/**
 *
 * Author:
 *
 * Created on: 2019/02/18
 *
 */
class TaskFetchAsync(val db: TaskDatabase) : AsyncTask<Int, Void, Task>(){

    override fun doInBackground(vararg p0: Int?): Task? {
        if (p0[0] != null) {
            return db.taskDao().getTask(p0[0]!!)
        } else return null
    }

    override fun onPostExecute(result: Task?) {
        super.onPostExecute(result)
    }
}