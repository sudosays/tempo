package com.sudosays.torro

import android.os.AsyncTask
import com.sudosays.torro.data.Task
import com.sudosays.torro.data.TaskDatabase

/**
 *
 * Author:
 *
 * Created on: 2019/02/18
 *
 */
class TaskFetchASync (val db: TaskDatabase) : AsyncTask<Void, Void, MutableList<Task>> (){

    override fun doInBackground(vararg p0: Void?): MutableList<Task>? {

        val taskList = db.taskDao().getAll()

        return taskList
    }

    override fun onPostExecute(result: MutableList<Task>?) {
        super.onPostExecute(result)
    }
}