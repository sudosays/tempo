package com.sudosays.tempo.taskutil

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

    private var sharedPreferences: SharedPreferences

    init {
        LayoutInflater.from(context).inflate(R.layout.edit_task, this, true)
        sharedPreferences = context.getSharedPreferences(resources.getString(R.string.settings_file_key), Context.MODE_PRIVATE)

        val durationArray = generateDurationSelections()

        ArrayAdapter(
                this.context,
                android.R.layout.simple_spinner_item,
                durationArray
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            durationSpinner.adapter = adapter
        }

    }

    fun populate(task: Task) {
        nameEditText.setText(task.name, TextView.BufferType.EDITABLE)
        durationSpinner.setSelection(task.duration - 1)
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

    private fun generateDurationSelections(num: Int = 8): Array<String> {
        return Array(num) { i -> convertDurationToTime(i+1) + " ("+ (i+1).toString() + " sessions)" }
    }

}