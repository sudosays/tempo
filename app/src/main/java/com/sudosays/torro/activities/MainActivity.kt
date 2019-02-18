package com.sudosays.torro.activities


import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sudosays.torro.R
import com.sudosays.torro.TaskArrayAdapter
import com.sudosays.torro.TaskFetchASync
import com.sudosays.torro.TaskInsertAsync
import com.sudosays.torro.data.Task
import com.sudosays.torro.data.TaskDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val ADD_TASK_REQUEST = 1
    private val taskMutableList = mutableListOf<Task>()
    private lateinit var listViewAdapter: TaskArrayAdapter

    private lateinit var db: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        db = TaskDatabase.getInstance(this)

        //insertTasks()

        taskMutableList.addAll(TaskFetchASync(db).execute().get())

        listViewAdapter = TaskArrayAdapter(this, R.layout.view_task, taskMutableList)
        taskListView.adapter = listViewAdapter
    }

    fun addTask(view: View){

        val intent = Intent(this, AddTask::class.java)
        startActivityForResult(intent, ADD_TASK_REQUEST)

    }

    fun startFlow(view: View) {
        val intent = Intent(this, FlowOverview::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    {

        if (requestCode == ADD_TASK_REQUEST)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                //taskMutableList.add(0,data.data?)
                listViewAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun insertTasks()
    {
        TaskInsertAsync(db).execute(Task(0,"Say Hello", 1.0f), Task(0,"Hello World", 1.0f),Task(0,"Do something else", 1.0f))
    }


}
