package com.sudosays.tempo.views

/**
 *
 * Author: Julius Stopforth
 *
 * Created on: 2018/06/02
 *
 */

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.widget.FrameLayout
import android.widget.LinearLayout
import com.sudosays.tempo.R
import com.sudosays.tempo.data.Task
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.view_task.view.*


class TaskView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
        ) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private lateinit var sharedPreferences: SharedPreferences

    init
    {
        LayoutInflater.from(context).inflate(R.layout.view_task, this, true)
        this.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        sharedPreferences = context.getSharedPreferences(resources.getString(R.string.settings_file_key), Context.MODE_PRIVATE)
    }

    fun populate(task: Task)
    {

        nameView.text = task.name
        durationView.text = convertDurationToTime(task.duration.toInt())

    }

    fun showSelected() {
        this.taskLinearLayout.isSelected = true
        invalidate()
    }

    fun deselect() {
        this.taskLinearLayout.isSelected = false
        invalidate()
    }

    private fun convertDurationToTime(duration: Int): String {
        val taskLength = sharedPreferences.getInt(resources.getString(R.string.task_length_key), resources.getInteger(R.integer.default_task_length))
        val shortBreakLength = sharedPreferences.getInt(resources.getString(R.string.short_break_key), resources.getInteger(R.integer.default_short_break_length))
        val longBreakLength = sharedPreferences.getInt(resources.getString(R.string.long_break_key), resources.getInteger(R.integer.default_long_break_length))

        var totaltime = duration*taskLength

        val numLongBreaks = if (duration <= 4) 0 else duration/4
        val numShortBreaks = duration - numLongBreaks - 1

        totaltime += numShortBreaks*shortBreakLength + numLongBreaks*longBreakLength

        var timeString:String

        if (totaltime/60 > 0) {
            timeString = "" + totaltime/60 + "h"
            if (totaltime%60 > 0) {
                timeString += "" + totaltime%60 + "min"
            }
        } else {
            timeString = "" + totaltime + "min"
        }

        return timeString
    }

}