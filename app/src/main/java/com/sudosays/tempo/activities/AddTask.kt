package com.sudosays.tempo.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.edit_task.view.*

import com.sudosays.tempo.R
import com.sudosays.tempo.TaskInsertAsync
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskDatabase

class AddTask : AppCompatActivity() {

    private lateinit var db: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        db = TaskDatabase.getInstance(this)
    }

    fun saveTask(view: View)
    {
        if ((taskEditview.nameEditText.text.isNotEmpty())&&(taskEditview.durationEditText.text.isNotEmpty())) {
            val task = Task(0, taskEditview.nameEditText.text.toString(), taskEditview.durationEditText.text.toString().toFloat())
            TaskInsertAsync(db).execute(task)
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
