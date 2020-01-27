package com.sudosays.tempo.activities

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.sudosays.tempo.R
import com.sudosays.tempo.async.TaskDeleteAsync
import com.sudosays.tempo.async.TaskFetchAllAsync
import com.sudosays.tempo.async.TaskUpdateAllAsync
import com.sudosays.tempo.async.TaskUpdateAsync
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskDatabase
import kotlinx.android.synthetic.main.activity_flow_overview.*
import java.util.*

class FlowOverview : AppCompatActivity() {

    var timeRemaining:Long = 0

    var isRunning = false
    var sessionCount = 0


    private lateinit var taskTimer: CountDownTimer
    private lateinit var shortBreakTimer: CountDownTimer
    private lateinit var longBreakTimer: CountDownTimer

    private lateinit var db: TaskDatabase
    private val todoList = mutableListOf<Task>()

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_overview)

        db = TaskDatabase.getInstance(this)
        todoList.addAll(TaskFetchAllAsync(db).execute().get())
        todoList?.let {
            currentTaskNameView.text = todoList[0].name
            nextTaskNameView.visibility = View.INVISIBLE
        }

        sharedPrefs = this.getSharedPreferences(getString(R.string.settings_file_key), Context.MODE_PRIVATE)
        val taskLength = sharedPrefs.getInt(getString(R.string.task_length_key), resources.getInteger(R.integer.default_task_length)).toLong() * 60000
        val shortBreakLength = sharedPrefs.getInt(getString(R.string.short_break_key), resources.getInteger(R.integer.default_short_break_length)).toLong() * 60000
        val longBreakLength = sharedPrefs.getInt(getString(R.string.long_break_key), resources.getInteger(R.integer.default_long_break_length)).toLong() * 60000

        timeRemainingView.text = formatTimeRemaining(taskLength)

        taskTimer = object: CountDownTimer(taskLength, 100) {

            override fun onFinish() {
                updateSession()
            }

            override fun onTick(p0: Long) {
                updateTimeRemaining(p0)
            }

        }

        shortBreakTimer = object: CountDownTimer(shortBreakLength, 100) {

            override fun onFinish() {
                startTaskTimer()
            }

            override fun onTick(p0: Long) {
                updateTimeRemaining(p0)
            }

        }

        longBreakTimer = object: CountDownTimer(longBreakLength, 100) {

            override fun onFinish() {
                startTaskTimer()
            }

            override fun onTick(p0: Long) {
                updateTimeRemaining(p0)
            }

        }

        setFocusColors();
    }

    fun startStopTimer(view: View) {

        if (!isRunning) {
            isRunning = true
            startTaskTimer()
            button.text = getString(R.string.button_stop_flow)
        } else {
            isRunning = false
            stopAllTimers()
            button.text = getString(R.string.button_start_flow)
        }

    }

    private fun updateTimeRemaining(timeLeft: Long) {
        timeRemaining = timeLeft
        //For now cause it's connected to a view
        timeRemainingView.text = formatTimeRemaining(timeRemaining)
    }

    private fun startTaskTimer() {
        currentTaskNameView.text = todoList[0].name
        nextTaskNameView.visibility = View.INVISIBLE
        taskTimer.start()
        setFocusColors()
    }

    private fun updateSession() {

        todoList[0].duration -= 1
        if (todoList[0].duration <= 0) {
            TaskDeleteAsync(db).execute(todoList.removeAt(0))
            decrementPositions()
            syncPositionsWithDb()
        } else {
            TaskUpdateAsync(db).execute(todoList[0])
        }

        if (todoList.isEmpty())
        {
            finish()
        } else {
            sessionCount += 1
            if (sessionCount >= 4) {
                startLongBreak()
                sessionCount = 0
            } else {
                startShortBreak()
            }
        }
    }

    private fun startLongBreak() {
        currentTaskNameView.text = getBreakMessage()
        nextTaskNameView.text = todoList[0].name
        nextTaskNameView.visibility = View.VISIBLE
        longBreakTimer.start()
        setLongBreakColors()
    }

    private fun startShortBreak() {
        currentTaskNameView.text = getBreakMessage()
        nextTaskNameView.text = todoList[0].name
        nextTaskNameView.visibility = View.VISIBLE
        shortBreakTimer.start()
        setShortBreakColors()
    }

    private fun formatTimeRemaining(t: Long): String {
        val minLeft = (t/60000).toInt()
        val secLeft = (t-minLeft*60000)/1000
        return "%02d:%02d".format(minLeft,secLeft)
    }

    private fun stopAllTimers(){
        taskTimer.cancel()
        shortBreakTimer.cancel()
        longBreakTimer.cancel()
    }

    override fun onStop() {
        super.onStop()
        stopAllTimers()
    }

    private fun getBreakMessage(): String {
        val messages = resources.getStringArray(R.array.break_messages)
        return messages.get(Random().nextInt(messages.size))
    }

    private fun decrementPositions() {
        for (t in todoList) {
            t.position -= 1
        }
        val lastPosition = sharedPrefs.getInt("last_position",0)

        with(sharedPrefs.edit()) {
            if (lastPosition > 0) {
                putInt("last_position", lastPosition - 1)
            } else {
                putInt("last_position", 0)
            }
        }
    }

    private fun syncPositionsWithDb() {
        TaskUpdateAllAsync(db).execute(*todoList.toTypedArray())
    }

    private fun setFocusColors() {

        flowParent.background = getDrawable(R.color.focusBackgroundColor)

    }

    private fun setShortBreakColors() {

        flowParent.background = getDrawable(R.color.shortBreakBackgroundColor)

    }

    private fun setLongBreakColors() {

        flowParent.background = getDrawable(R.color.longBreakBackgroundColor)

    }


}
