package com.sudosays.tempo.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.sudosays.tempo.R
import kotlinx.android.synthetic.main.activity_flow_settings.*

class FlowSettings : AppCompatActivity() {

    private lateinit var sharedPrefs:SharedPreferences

    private val taskIncrement = resources.getInteger(R.integer.default_task_increment)
    private val shortBreakIncrement = resources.getInteger(R.integer.default_short_break_increment)
    private val longBreakIncrement = resources.getInteger(R.integer.default_long_break_increment)
    private val defaultTaskLength = resources.getInteger(R.integer.default_task_length)
    private val defaultShortBreakLength = resources.getInteger(R.integer.default_short_break_length)
    private val defaultLongBreakLength = resources.getInteger(R.integer.default_long_break_length)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_settings)
        sharedPrefs = this.getSharedPreferences(getString(R.string.settings_file_key),Context.MODE_PRIVATE)

        setSeekBars()
        setLabels()
    }

    fun saveSettings(view: View)
    {

        with(sharedPrefs.edit())
        {
            putInt(getString(R.string.task_length_key), defaultTaskLength + (taskTimeSeekBar.progress*taskIncrement))
            putInt(getString(R.string.short_break_key), defaultShortBreakLength + (shortBreakSeekBar.progress*shortBreakIncrement))
            putInt(getString(R.string.long_break_key), defaultLongBreakLength + (longBreakSeekBar.progress*longBreakIncrement))
            apply()
        }
        Toast.makeText(this.applicationContext,getString(R.string.feedback_saved_settings),Toast.LENGTH_SHORT).show()
        finish()
    }

    fun cancel(view: View)
    {
        finish()
    }

    private fun setSeekBars(){

        var taskLength = sharedPrefs.getInt(getString(R.string.task_length_key),resources.getInteger(R.integer.default_task_length))
        var shortBreakLength = sharedPrefs.getInt(getString(R.string.short_break_key), resources.getInteger(R.integer.default_short_break_length))
        var longBreakLength = sharedPrefs.getInt(getString(R.string.long_break_key), resources.getInteger(R.integer.default_long_break_length))

        if (taskLength == defaultTaskLength)
        {
            taskTimeSeekBar.progress = 0
        } else
        {
            taskTimeSeekBar.progress = (taskLength - defaultTaskLength)/taskIncrement
        }

        if (shortBreakLength == defaultShortBreakLength)
        {
            shortBreakSeekBar.progress = 0
        } else
        {
            shortBreakSeekBar.progress = (shortBreakLength - defaultShortBreakLength)/shortBreakIncrement
        }

        if (longBreakLength == defaultLongBreakLength)
        {
            longBreakSeekBar.progress = 0
        } else
        {
            longBreakSeekBar.progress = (longBreakLength - defaultLongBreakLength)/longBreakIncrement
        }

    }

    private fun setLabels(){

        val updatedTaskLabel = taskTimeTextView.text.toString() + " = " + (defaultTaskLength + (taskTimeSeekBar.progress*taskIncrement))
        val updatedShortBreakLabel = shortBreakTextView.text.toString() + " = " + (defaultShortBreakLength + (shortBreakSeekBar.progress*shortBreakIncrement)).toString()
        val updatedLongBreakLabel = longBreakTextView.text.toString() + " = " + (defaultLongBreakLength + (longBreakSeekBar.progress*longBreakIncrement)).toString()

        taskTimeTextView.setText(updatedTaskLabel)
        shortBreakTextView.setText(updatedShortBreakLabel)
        longBreakTextView.setText(updatedLongBreakLabel)
    }


}
