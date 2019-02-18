package com.sudosays.torro.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.edit_task.view.*

import com.sudosays.torro.R
import com.sudosays.torro.data.Task

class AddTask : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
    }

    fun saveTask(view: View)
    {

        val task = Task(0,taskEditview.nameEditText.text.toString(), taskEditview.durationEditText.text.toString().toFloat())


    }
}
