package com.sudosays.tempo.flow

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import com.sudosays.tempo.R
import com.sudosays.tempo.async.TaskDeleteAsync
import com.sudosays.tempo.async.TaskFetchAllAsync
import com.sudosays.tempo.async.TaskUpdateAllAsync
import com.sudosays.tempo.async.TaskUpdateAsync
import com.sudosays.tempo.data.Task
import com.sudosays.tempo.data.TaskDatabase

import java.util.*

class FlowService : Service() {

    enum class SessionType {
        FOCUS,
        SHORT_BREAK,
        LONG_BREAK
    }

    var isRunning = false
        private set

    /// This is a foreground service and as such needs a Notification designed for it.
    private var timeRemaining: Long = 0

    var currentSessionType: SessionType = SessionType.FOCUS
        private set

    lateinit var currentText: String
        private set

    private var sessionCount = 0

    private val binder = FlowBinder()
    inner class FlowBinder : Binder() {
        fun getService(): FlowService = this@FlowService
    }

    private lateinit var taskTimer: CountDownTimer
    private lateinit var shortBreakTimer: CountDownTimer
    private lateinit var longBreakTimer: CountDownTimer

    private lateinit var db: TaskDatabase
    private val todoList = mutableListOf<Task>()

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        //resetService()
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onDestroy() {
        //stopAllTimers()

        //Toast.makeText(this.applicationContext, "FlowService Timers Stopped & Service Destroyed.", Toast.LENGTH_LONG).show()

        super.onDestroy()
    }

    /**--- UTIL FUNCTIONS ---**/

    private fun updateTimeRemaining(timeLeft: Long) {
        timeRemaining = timeLeft
    }

    fun startTaskTimer() {
        taskTimer.start()
        currentSessionType = SessionType.FOCUS
        isRunning = true
        stopForeground(true)
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

        if (todoList.isEmpty()) {
            stopSelf()
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
        longBreakTimer.start()
        currentText = getBreakMessage()
        currentSessionType = SessionType.LONG_BREAK
    }

    private fun startShortBreak() {
        shortBreakTimer.start()
        currentText = getBreakMessage()
        currentSessionType = SessionType.SHORT_BREAK
    }

    private fun formatTimeRemaining(t: Long): String {
        val minLeft = (t / 60000).toInt()
        val secLeft = (t - minLeft * 60000) / 1000
        return "%02d:%02d".format(minLeft, secLeft)
    }

    fun getTimeRemaining(): String {
        return formatTimeRemaining(timeRemaining)
    }

    fun stopAllTimers() {
        taskTimer.cancel()
        shortBreakTimer.cancel()
        longBreakTimer.cancel()
        isRunning = false
    }

    fun getNextTaskName(): String {
        return todoList[0].name
    }

    private fun getBreakMessage(): String {
        val messages = resources.getStringArray(R.array.break_messages)
        return messages[Random().nextInt(messages.size)]
    }

    private fun decrementPositions() {
        for (t in todoList) {
            t.position -= 1
        }
        val lastPosition = sharedPrefs.getInt("last_position", 0)

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

    fun resetService() {
        db = TaskDatabase.getInstance(this)
        todoList.clear()
        todoList.addAll(TaskFetchAllAsync(db).execute().get())
        if (todoList.isNotEmpty()) {
            todoList?.let {
                currentText = todoList[0].name
            }
        } else {
            currentText = "None"
        }

        sharedPrefs = this.getSharedPreferences(getString(R.string.settings_file_key), Context.MODE_PRIVATE)
        val taskLength = sharedPrefs.getInt(getString(R.string.task_length_key), resources.getInteger(R.integer.default_task_length)).toLong() * 60000
        val shortBreakLength = sharedPrefs.getInt(getString(R.string.short_break_key), resources.getInteger(R.integer.default_short_break_length)).toLong() * 60000
        val longBreakLength = sharedPrefs.getInt(getString(R.string.long_break_key), resources.getInteger(R.integer.default_long_break_length)).toLong() * 60000

        taskTimer = object : CountDownTimer(taskLength, 100) {

            override fun onFinish() {
                updateSession()
            }

            override fun onTick(p0: Long) {
                updateTimeRemaining(p0)
            }

        }
        shortBreakTimer = object : CountDownTimer(shortBreakLength, 100) {

            override fun onFinish() {
                startTaskTimer()
            }

            override fun onTick(p0: Long) {
                updateTimeRemaining(p0)
            }

        }
        longBreakTimer = object : CountDownTimer(longBreakLength, 100) {

            override fun onFinish() {
                startTaskTimer()
            }

            override fun onTick(p0: Long) {
                updateTimeRemaining(p0)
            }

        }


    }

}