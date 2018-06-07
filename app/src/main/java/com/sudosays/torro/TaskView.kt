package com.sudosays.torro

/**
 *
 * Author: Julius Stopforth
 *
 * Created on: 2018/06/02
 *
 */

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.view_task.*
import kotlinx.android.synthetic.main.view_task.view.*


class TaskView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
        ) : FrameLayout(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_task, this, true)
        //orientation = HORIZONTAL
    }

    fun populate(task: Task) {

        nameView.text = task.name
        durationView.text = task.duration.toString()

    }

}