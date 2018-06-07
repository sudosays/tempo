package com.sudosays.torro

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val t = Task("Say Hello", 1.0f)

        singleTask.populate(t)

        /*val testView:TaskView = TaskView(this)



        container.addView(testView)

        testView.populate(t)*/


    }

    fun changeTaskInfo(view: View) {

        val t:Task = Task("This has changed", 0.75f)

        singleTask.populate(t)

    }

}
