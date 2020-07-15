package com.sudosays.tempo.main

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.sudosays.tempo.R
import kotlinx.android.synthetic.main.activity_flow_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences

    private val taskIncrement by lazy { resources.getInteger(R.integer.default_task_increment) }
    private val shortBreakIncrement by lazy { resources.getInteger(R.integer.default_short_break_increment) }
    private val longBreakIncrement by lazy { resources.getInteger(R.integer.default_long_break_increment) }
    private val defaultTaskLength by lazy { resources.getInteger(R.integer.default_task_length) }
    private val defaultShortBreakLength by lazy { resources.getInteger(R.integer.default_short_break_length) }
    private val defaultLongBreakLength by lazy { resources.getInteger(R.integer.default_long_break_length) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_settings)
        sharedPrefs = this.getSharedPreferences(getString(R.string.settings_file_key), Context.MODE_PRIVATE)

        val seekbarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                when (seekBar) {
                    taskTimeSeekBar -> updateTaskLabel()
                    shortBreakSeekBar -> updateShortBreakLabel()
                    longBreakSeekBar -> updateLongBreakLabel()
                    else -> Toast.makeText(applicationContext, "What Seekbar are you pressing?!?!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }

        taskTimeSeekBar.setOnSeekBarChangeListener(seekbarChangeListener)
        shortBreakSeekBar.setOnSeekBarChangeListener(seekbarChangeListener)
        longBreakSeekBar.setOnSeekBarChangeListener(seekbarChangeListener)

        setSeekBars()
        setLabels()
    }

    fun saveSettings(view: View) {

        with(sharedPrefs.edit())
        {
            putInt(getString(R.string.task_length_key), defaultTaskLength + (taskTimeSeekBar.progress * taskIncrement))
            putInt(getString(R.string.short_break_key), defaultShortBreakLength + (shortBreakSeekBar.progress * shortBreakIncrement))
            putInt(getString(R.string.long_break_key), defaultLongBreakLength + (longBreakSeekBar.progress * longBreakIncrement))
            apply()
        }
        Toast.makeText(this.applicationContext, getString(R.string.feedback_saved_settings), Toast.LENGTH_SHORT).show()
        finish()
    }

    fun cancel(view: View) {
        finish()
    }

    private fun setSeekBars() {

        var taskLength = sharedPrefs.getInt(getString(R.string.task_length_key), defaultTaskLength)
        var shortBreakLength = sharedPrefs.getInt(getString(R.string.short_break_key), defaultShortBreakLength)
        var longBreakLength = sharedPrefs.getInt(getString(R.string.long_break_key), defaultLongBreakLength)

        taskTimeSeekBar.progress = (taskLength - defaultTaskLength) / taskIncrement
        shortBreakSeekBar.progress = (shortBreakLength - defaultShortBreakLength) / shortBreakIncrement
        longBreakSeekBar.progress = (longBreakLength - defaultLongBreakLength) / longBreakIncrement

    }

    private fun setLabels() {
        updateTaskLabel()
        updateShortBreakLabel()
        updateLongBreakLabel()
    }

    private fun updateTaskLabel() {

        val updatedTaskLabel = resources.getString(R.string.task_length_prompt) + " = " + (defaultTaskLength + (taskTimeSeekBar.progress * taskIncrement)) + "min"
        taskTimeTextView.text = updatedTaskLabel

    }

    private fun updateShortBreakLabel() {

        val updatedShortBreakLabel = resources.getString(R.string.short_break_prompt) + " = " + (defaultShortBreakLength + (shortBreakSeekBar.progress * shortBreakIncrement)).toString() + "min"
        shortBreakTextView.text = updatedShortBreakLabel

    }

    private fun updateLongBreakLabel() {

        val updatedLongBreakLabel = resources.getString(R.string.long_break_prompt) + " = " + (defaultLongBreakLength + (longBreakSeekBar.progress * longBreakIncrement)).toString() + "min"
        longBreakTextView.text = updatedLongBreakLabel

    }


}
