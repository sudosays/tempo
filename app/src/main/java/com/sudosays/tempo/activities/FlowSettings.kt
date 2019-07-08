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

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_settings)
        sharedPrefs = this.getSharedPreferences(getString(R.string.settings_file_key),Context.MODE_PRIVATE)
        setSeekBars()
    }

    fun saveSettings(view: View)
    {
        val taskIncrement = resources.getInteger(R.integer.default_task_increment)
        val shortBreakIncrement = resources.getInteger(R.integer.default_short_break_increment)
        val longBreakIncrement = resources.getInteger(R.integer.default_long_break_increment)
        val taskLength = resources.getInteger(R.integer.default_task_length)
        val shortBreakLength = resources.getInteger(R.integer.default_short_break_length)
        val longBreakLength = resources.getInteger(R.integer.default_long_break_length)

        with(sharedPrefs.edit())
        {
            putInt(getString(R.string.task_length_key), taskLength + (taskTimeSeekBar.progress*taskIncrement))
            putInt(getString(R.string.short_break_key), shortBreakLength + (shortBreakSeekBar.progress*shortBreakIncrement))
            putInt(getString(R.string.long_break_key), longBreakLength + (longBreakSeekBar.progress*longBreakIncrement))
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

        val taskIncrement = resources.getInteger(R.integer.default_task_increment)
        val shortBreakIncrement = resources.getInteger(R.integer.default_short_break_increment)
        val longBreakIncrement = resources.getInteger(R.integer.default_long_break_increment)
        val setTaskLength = sharedPrefs.getInt(getString(R.string.task_length_key),resources.getInteger(R.integer.default_task_length))
        val setShortBreakLength = sharedPrefs.getInt(getString(R.string.short_break_key), resources.getInteger(R.integer.default_short_break_length))
        val setLongBreakLength = sharedPrefs.getInt(getString(R.string.long_break_key), resources.getInteger(R.integer.default_long_break_length))

        if ((setTaskLength - resources.getInteger(R.integer.default_task_length)) == 0)
        {
            taskTimeSeekBar.progress = 0
        } else
        {
            taskTimeSeekBar.progress = (setTaskLength - resources.getInteger(R.integer.default_task_length))/taskIncrement
        }

        if ((setShortBreakLength - resources.getInteger(R.integer.default_short_break_length)) == 0)
        {
            shortBreakSeekBar.progress = 0
        } else
        {
            shortBreakSeekBar.progress = (setShortBreakLength - resources.getInteger(R.integer.default_short_break_length))/shortBreakIncrement
        }

        if ((setLongBreakLength - resources.getInteger(R.integer.default_long_break_length)) == 0)
        {
            longBreakSeekBar.progress = 0
        } else
        {
            longBreakSeekBar.progress = (setLongBreakLength - resources.getInteger(R.integer.default_long_break_length))/longBreakIncrement
        }

    }
}
