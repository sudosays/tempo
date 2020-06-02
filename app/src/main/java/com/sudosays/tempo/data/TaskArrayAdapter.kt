package com.sudosays.tempo.data

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.sudosays.tempo.taskutil.TaskView

/**
 *
 * Author: Julius Stopforth
 *
 * Created on: 2019/02/13
 *
 */
class TaskArrayAdapter constructor(
        context: Context,
        resource: Int,
        objects: MutableList<Task>
) : ArrayAdapter<Task>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val taskView = TaskView(parent!!.context)
        taskView.populate(getItem(position))
        return taskView

        //return super.getView(position, convertView, parent)
    }

}