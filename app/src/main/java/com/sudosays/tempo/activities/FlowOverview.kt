package com.sudosays.tempo.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.sudosays.tempo.R
import com.sudosays.tempo.TaskDeleteAsync
import com.sudosays.tempo.TaskFetchASync
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskDatabase
import kotlinx.android.synthetic.main.activity_flow_overview.*

class FlowOverview : AppCompatActivity() {

    private val TASK_LENGTH:Long = 60000
    private val SHORT_BREAK_LENGTH:Long = 10000
    private val LONG_BREAK_LENGTH:Long = 30000
    var timeRemaining:Long = 0

    var isRunning = false
    var sessionCount = 0


    private var taskTimer: CountDownTimer
    private var shortBreakTimer: CountDownTimer
    private var longBreakTimer: CountDownTimer

    private lateinit var db: TaskDatabase
    private val todoList = mutableListOf<Task>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_overview)

        db = TaskDatabase.getInstance(this)
        todoList.addAll(TaskFetchASync(db).execute().get())
        todoList?.let {
            currentTaskNameView.text = todoList[0].name
            nextTaskNameView.visibility = View.INVISIBLE
        }
    }

    init {
        taskTimer = object: CountDownTimer(TASK_LENGTH, 100) {

            override fun onFinish() {
                updateSession()
            }

            override fun onTick(p0: Long) {
                updateTimeRemaining(p0)
            }

        }

        shortBreakTimer = object: CountDownTimer(SHORT_BREAK_LENGTH, 100) {

            override fun onFinish() {
                startTaskTimer()
            }

            override fun onTick(p0: Long) {
                updateTimeRemaining(p0)
            }

        }

        longBreakTimer = object: CountDownTimer(LONG_BREAK_LENGTH, 100) {

            override fun onFinish() {
                startTaskTimer()
            }

            override fun onTick(p0: Long) {
                updateTimeRemaining(p0)
            }

        }
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
    }

    private fun updateSession() {

        TaskDeleteAsync(db).execute(todoList.removeAt(0))
        if (todoList.isEmpty())
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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
        currentTaskNameView.text = "Long break"
        nextTaskNameView.text = todoList[0].name
        nextTaskNameView.visibility = View.VISIBLE
        longBreakTimer.start()
    }

    private fun startShortBreak() {
        currentTaskNameView.text = "Short break"
        nextTaskNameView.text = todoList[0].name
        nextTaskNameView.visibility = View.VISIBLE
        shortBreakTimer.start()
    }

    private fun formatTimeRemaining(t: Long): String {
        val minLeft = (t/60000).toInt()
        val secLeft = (t-minLeft*60000)/1000
        return "%02d:%02d".format(minLeft,secLeft)
        //val output:Float = ((t)/1000f)
        //return "%.3f".format(output)

    }

    private fun stopAllTimers(){
        taskTimer.cancel()
        shortBreakTimer.cancel()
        longBreakTimer.cancel()
        timeRemainingView.text = getString(R.string.flow_timer_placeholder)
    }

    override fun onStop() {
        super.onStop()
        stopAllTimers()
    }

}
