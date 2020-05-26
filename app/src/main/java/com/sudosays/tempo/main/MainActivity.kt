package com.sudosays.tempo.main


import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.sudosays.tempo.*
import com.sudosays.tempo.async.TaskFetchAllAsync
import com.sudosays.tempo.async.TaskUpdateAllAsync
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskArrayAdapter
import com.sudosays.tempo.data.TaskDatabase
import com.sudosays.tempo.flow.FlowOverviewActivity
import com.sudosays.tempo.taskutil.AddTaskActivity
import com.sudosays.tempo.taskutil.EditTaskActivity
import com.sudosays.tempo.taskutil.TaskView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val ADD_TASK_REQUEST = 1
    private val EDIT_TASK_REQUEST = 2
    private val taskMutableList = mutableListOf<Task>()
    private lateinit var listViewAdapter: TaskArrayAdapter

    private lateinit var db: TaskDatabase

    private var selectedTaskPosition:Int = -1

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
            switchToEditMode(position, view as TaskView)
        }
    }

    fun addTask(view: View)
    {

        val intent = Intent(this, AddTaskActivity::class.java)
        startActivityForResult(intent, ADD_TASK_REQUEST)

    }

    fun startFlow(view: View)
    {
        if (taskMutableList.isNotEmpty())
        {
            val intent = Intent(this, FlowOverviewActivity::class.java)
            startActivity(intent)
        } else
        {
            Toast.makeText(this,R.string.start_flow_help, Toast.LENGTH_SHORT).show()
        }
    }

    fun openSettings(view: View)
    {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    {

        when (requestCode) {
            ADD_TASK_REQUEST -> if (resultCode == Activity.RESULT_OK) {reloadTasks()}
            EDIT_TASK_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                reloadTasks()
                switchToOverviewMode(editTaskButton)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                reloadTasks()
            // If the task was deleted
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                if (taskMutableList.size != 0) {
                    for (i in selectedTaskPosition..(taskMutableList.size - 1)) {
                        taskMutableList[i].position -= 1
                    }
                    syncPositionsWithDb()
                }
            }

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

    private fun switchToEditMode(taskPosition: Int, taskView: TaskView) {
        startFlowButton.visibility = View.GONE
        addTaskButton.visibility = View.GONE
        settingsButton.visibility = View.GONE
        editTaskButton.visibility = View.VISIBLE
        moveUpButton.visibility = View.VISIBLE
        moveDownButton.visibility = View.VISIBLE
        exitEditModeButton.visibility = View.VISIBLE

        if (selectedTaskPosition != taskPosition && selectedTaskPosition >= 0) {
            val oldTask = taskListView.getChildAt(selectedTaskPosition) as TaskView
            oldTask.deselect()

        }

        taskView.showSelected()
        selectedTaskPosition = taskPosition

    }

    fun switchToOverviewMode(view: View) {
        startFlowButton.visibility = View.VISIBLE
        addTaskButton.visibility = View.VISIBLE
        settingsButton.visibility = View.VISIBLE
        editTaskButton.visibility = View.GONE
        moveUpButton.visibility = View.GONE
        moveDownButton.visibility = View.GONE
        exitEditModeButton.visibility = View.GONE

        if (taskMutableList.size > 0) {
            setTaskViewSelected(selectedTaskPosition, false)
            syncPositionsWithDb()
        }

        selectedTaskPosition = -1
        syncPositionsWithDb()
    }

    fun editTask(view: View) {
        val intent = Intent(this, EditTaskActivity::class.java)
        intent.putExtra("taskid", listViewAdapter.getItem(selectedTaskPosition).uid)
        startActivityForResult(intent, EDIT_TASK_REQUEST)
    }

    fun moveTaskUp(view: View) {

        if (selectedTaskPosition > 0 && listViewAdapter.count > 1) {
            taskMutableList[selectedTaskPosition].position -= 1
            taskMutableList[selectedTaskPosition - 1].position += 1

            taskMutableList.add(selectedTaskPosition - 1, taskMutableList.removeAt(selectedTaskPosition))
            listViewAdapter.notifyDataSetChanged()
            selectedTaskPosition -= 1
            setTaskViewSelected(selectedTaskPosition, true)
        }
    }

    fun moveTaskDown(view: View) {

        if (selectedTaskPosition < taskMutableList.lastIndex && listViewAdapter.count > 1) {
            taskMutableList[selectedTaskPosition].position += 1
            taskMutableList[selectedTaskPosition + 1].position -= 1

            taskMutableList.add(selectedTaskPosition + 1, taskMutableList.removeAt(selectedTaskPosition))
            listViewAdapter.notifyDataSetChanged()

            selectedTaskPosition += 1
            setTaskViewSelected(selectedTaskPosition, true)

        }
    }

    private fun syncPositionsWithDb() {
        TaskUpdateAllAsync(db).execute(*taskMutableList.toTypedArray())
    }

    private fun setTaskViewSelected(position:Int, isSelected: Boolean) {
        val taskView = taskListView.getChildAt(position) as TaskView
        if (isSelected) {
            taskView.showSelected()
        } else {
            taskView.deselect()
        }
    }
}
