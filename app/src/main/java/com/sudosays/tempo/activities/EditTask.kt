package com.sudosays.tempo.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.sudosays.tempo.R
import com.sudosays.tempo.async.TaskDeleteAsync
import com.sudosays.tempo.async.TaskFetchAsync
import com.sudosays.tempo.async.TaskUpdateAsync
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskDatabase
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.edit_task.view.*

class EditTask : AppCompatActivity() {

    private lateinit var db: TaskDatabase
    private lateinit var taskToEdit: Task

    private lateinit var sharedPrefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        db = TaskDatabase.getInstance(this)
        sharedPrefs = this.getSharedPreferences(getString(R.string.settings_file_key), Context.MODE_PRIVATE)


        taskToEdit = TaskFetchAsync(db).execute(intent.extras["taskid"] as Int).get()
        taskEditview.populate(taskToEdit)
    }

    fun saveTask(view: View)
    {
        if ((taskEditview.nameEditText.text.isNotEmpty())&&(taskEditview.durationEditText.text.isNotEmpty())) {
            taskToEdit.name = taskEditview.nameEditText.text.toString()
            taskToEdit.duration = taskEditview.durationEditText.text.toString().toInt()
            TaskUpdateAsync(db).execute(taskToEdit)
            setResult(Activity.RESULT_OK, Intent())
            finish()
        } else
        {
            Toast.makeText(this.applicationContext, "Task cannot be blank.\nDid you mean delete?", Toast.LENGTH_LONG).show()
        }

    }

    fun cancel(view: View)
    {
        setResult(Activity.RESULT_CANCELED, Intent())
        finish()
    }

    fun deleteTask(view: View) {
        TaskDeleteAsync(db).execute(taskToEdit)
        setResult(Activity.RESULT_FIRST_USER, Intent())

        var lastPostition = sharedPrefs.getInt("last_position", 0)

        with (sharedPrefs.edit()) {
            if (lastPostition > 0) {
                putInt("last_position", lastPostition - 1)
                apply()
            }
        }

        finish()
    }
}