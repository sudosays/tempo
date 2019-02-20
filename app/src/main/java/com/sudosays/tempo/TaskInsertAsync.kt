package com.sudosays.tempo

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
class TaskInsertAsync(val db: TaskDatabase) : AsyncTask<Task, Void, Boolean> (){

    override fun doInBackground(vararg p0: Task): Boolean {

        db.taskDao().insertAll(*p0)
        return true

    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
    }

}