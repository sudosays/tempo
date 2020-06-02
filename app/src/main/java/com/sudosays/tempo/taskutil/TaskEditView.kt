package com.sudosays.tempo.taskutil

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.TextView
import com.sudosays.tempo.R
import com.sudosays.tempo.data.Task
import kotlinx.android.synthetic.main.edit_task.view.*

/**
 *
 * Author:
 *
 * Created on: 2018/09/16
 *
 */
class TaskEditView @JvmOverloads constructor(context: Context,
                                             attrs: AttributeSet? = null,
                                             defStyle: Int = 0
        //defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private lateinit var sharedPreferences: SharedPreferences

    init {
        LayoutInflater.from(context).inflate(R.layout.edit_task, this, true)
        sharedPreferences = context.getSharedPreferences(resources.getString(R.string.settings_file_key), Context.MODE_PRIVATE)
        durationEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val duration = p0.toString().toIntOrNull()

                summaryTextView.text = ""

                duration?.let {
                    summaryTextView.text = convertDurationToTime(it)
                }
            }

        })

    }

    fun populate(task: Task) {
        nameEditText.setText(task.name, TextView.BufferType.EDITABLE)
        durationEditText.setText(task.duration.toString(), TextView.BufferType.EDITABLE)

    }


    /*fun updateSummary(duration:Int) {
        taskTime = duration*25
        summaryTextView.text = "HA!"
    }*/

    private fun convertDurationToTime(duration: Int): String {
        val taskLength = sharedPreferences.getInt(resources.getString(R.string.task_length_key), resources.getInteger(R.integer.default_task_length))
        val shortBreakLength = sharedPreferences.getInt(resources.getString(R.string.short_break_key), resources.getInteger(R.integer.default_short_break_length))
        val longBreakLength = sharedPreferences.getInt(resources.getString(R.string.long_break_key), resources.getInteger(R.integer.default_long_break_length))

        var totaltime = duration * taskLength

        val numLongBreaks = if (duration <= 4) 0 else duration / 4
        val numShortBreaks = duration - numLongBreaks - 1

        totaltime += numShortBreaks * shortBreakLength + numLongBreaks * longBreakLength

        var timeString: String

        if (totaltime / 60 > 0) {
            timeString = "" + totaltime / 60 + "h"
            if (totaltime % 60 > 0) {
                timeString += "" + totaltime % 60 + "min"
            }
        } else {
            timeString = "" + totaltime + "min"
        }

        return timeString
    }


}