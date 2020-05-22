package com.sudosays.tempo.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.edit_task.view.*

import com.sudosays.tempo.R
import com.sudosays.tempo.async.TaskInsertAsync
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskDatabase

class AddTask : AppCompatActivity() {

    private lateinit var db: TaskDatabase

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        db = TaskDatabase.getInstance(this)
        sharedPrefs = this.getSharedPreferences(getString(R.string.settings_file_key), Context.MODE_PRIVATE)

    }

    fun saveTask(view: View)
    {
        if ((taskEditView.nameEditText.text.isNotEmpty())&&(taskEditView.durationEditText.text.isNotEmpty())) {
            val last_position = sharedPrefs.getInt("last_position",0)
            val task = Task(0, taskEditView.nameEditText.text.toString(), taskEditView.durationEditText.text.toString().toInt(), last_position)
            TaskInsertAsync(db).execute(task)

            with(sharedPrefs.edit()) {
                putInt("last_position", last_position + 1)
                apply()
            }

            setResult(Activity.RESULT_OK, Intent())
            finish()
        } else
        {
            Toast.makeText(this.applicationContext,"You need to fill out the task!", Toast.LENGTH_SHORT).show()
        }

    }

    fun cancel(view: View)
    {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }
}
