package com.sudosays.tempo.activities


import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.sudosays.tempo.*
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val ADD_TASK_REQUEST = 1
    private val EDIT_TASK_REQUEST = 2
    private val taskMutableList = mutableListOf<Task>()
    private lateinit var listViewAdapter: TaskArrayAdapter

    private lateinit var db: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        db = TaskDatabase.getInstance(this)

        //insertTasks()

        taskMutableList.addAll(TaskFetchAllAsync(db).execute().get())

        listViewAdapter = TaskArrayAdapter(this, R.layout.view_task, taskMutableList)
        taskListView.adapter = listViewAdapter

        taskListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, EditTask::class.java)
            intent.putExtra("taskid", listViewAdapter.getItem(position).uid)
            startActivityForResult(intent, EDIT_TASK_REQUEST)
        }
    }

    fun addTask(view: View)
    {

        val intent = Intent(this, AddTask::class.java)
        startActivityForResult(intent, ADD_TASK_REQUEST)

    }

    fun startFlow(view: View)
    {
        if (taskMutableList.isNotEmpty())
        {
            val intent = Intent(this, FlowOverview::class.java)
            startActivity(intent)
        } else
        {
            Toast.makeText(this,R.string.start_flow_help, Toast.LENGTH_SHORT).show()
        }
    }

    fun openSettings(view: View)
    {
        val intent = Intent(this, FlowSettings::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    {

        when (requestCode) {
            ADD_TASK_REQUEST -> if (resultCode == Activity.RESULT_OK) {reloadTasks()}
            EDIT_TASK_REQUEST -> if (resultCode == Activity.RESULT_OK) {reloadTasks()}
        }

    }

    override fun onResume()
    {
        super.onResume()
        reloadTasks()
    }

    private fun reloadTasks()
    {
        taskMutableList.clear()
        taskMutableList.addAll(TaskFetchAllAsync(db).execute().get())
        listViewAdapter.notifyDataSetChanged()
    }

}
