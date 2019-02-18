package com.sudosays.torro.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.sudosays.torro.R
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_overview)
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

    fun startTimer(view: View) {

        if (!isRunning) {
            isRunning = true
            startTaskTimer()
        }

    }

    private fun updateTimeRemaining(timeLeft: Long) {
        timeRemaining = timeLeft
        //For now cause it's connected to a view
        timeRemainingView.text = formatTimeRemaining(timeRemaining)
    }

    private fun startTaskTimer() {

        taskTimer.start()
    }

    private fun updateSession() {

        sessionCount += 1
        if (sessionCount >= 4) {
            startLongBreak()
            sessionCount = 0
        } else {
            startShortBreak()
        }

    }

    private fun startLongBreak() {
        longBreakTimer.start()
    }

    private fun startShortBreak() {
        shortBreakTimer.start()
    }

    private fun formatTimeRemaining(t: Long): String {
        /*val minLeft = (t/60000).toInt()
        val secLeft = (t-minLeft*60000)/1000
        return "%02d:%02d".format(minLeft,secLeft)*/
        val output:Float = ((t)/1000f)
        return "%.3f".format(output)

    }

}
