package com.sudosays.torro


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val taskMutableList = mutableListOf<Task>()
    lateinit var listViewAdapter: TaskArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        taskMutableList.add(Task("Say Hello", 1.0f))
        taskMutableList.add(Task("Hello World", 1.0f))

        taskMutableList.add(Task("Do something else", 1.0f))


        listViewAdapter = TaskArrayAdapter(this, R.layout.view_task, taskMutableList)
        taskListView.adapter = listViewAdapter
    }

    fun addTask(view: View){

        val intent = Intent(this, AddTask::class.java)
        startActivity(intent)

    }

    fun startFlow(view: View) {
        val intent = Intent(this, FlowOverview::class.java)
        startActivity(intent)
    }


}
