package com.sudosays.tempo.flow

import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import com.sudosays.tempo.R
import kotlinx.android.synthetic.main.activity_flow_overview.*

class FlowOverviewActivity : AppCompatActivity() {

    private lateinit var uiUpdateHandler: Handler
    private lateinit var uiUpdateRunnable: Runnable

    private lateinit var flowService: FlowService
    private var serviceBound = false
    private var currentSessionType: FlowService.SessionType = FlowService.SessionType.LONG_BREAK

    private val flowServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as FlowService.FlowBinder
            flowService = binder.getService()
            serviceBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            serviceBound = false
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_overview)

        uiUpdateHandler = Handler()
        uiUpdateRunnable = Runnable {
            try {
                updateUI()
            } finally {
                uiUpdateHandler.postDelayed(uiUpdateRunnable, 500)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        Intent(this, FlowService::class.java).also { intent ->
            //startService(intent)
            bindService(intent, flowServiceConnection, Context.BIND_AUTO_CREATE)
        }

        uiUpdateRunnable.run()

    }

    fun startStopTimer(view: View) {

        if (serviceBound) {
            if (!flowService.isRunning) {
                flowService.resetService()
                flowService.startTaskTimer()
                button.text = getString(R.string.button_stop_flow)

            } else {
                flowService.stopAllTimers()
                button.text = getString(R.string.button_start_flow)

            }
        } else {
            Log.e("Service", "Unbound service. Flow service does not exist!")
        }

    }

    private fun updateTimeRemaining() {
        timeRemainingView.text = flowService.getTimeRemaining()
    }

    private fun updateUI() {
        if (serviceBound) {
            if (currentSessionType != flowService.currentSessionType) {
                when (flowService.currentSessionType) {
                    FlowService.SessionType.FOCUS -> setFocusUI()
                    FlowService.SessionType.SHORT_BREAK -> setShortBreakUI()
                    FlowService.SessionType.LONG_BREAK -> setLongBreakColors()
                }

                currentSessionType = flowService.currentSessionType
            }

            if (flowService.isRunning) {
                button.text = getString(R.string.button_stop_flow)
            } else {
                button.text = getString(R.string.button_start_flow)
            }

            updateTimeRemaining()
            if (button.visibility != View.VISIBLE) {
                button.visibility = View.VISIBLE
            }

        }


    }

    override fun onStop() {
        super.onStop()

        uiUpdateHandler.removeCallbacks(uiUpdateRunnable)
    }

    private fun setFocusUI() {

        flowParent.background = getDrawable(R.color.focusBackgroundColor)
        currentTaskNameView.text = flowService.currentText
        nextTaskNameView.visibility = View.INVISIBLE


    }

    private fun setShortBreakUI() {

        flowParent.background = getDrawable(R.color.shortBreakBackgroundColor)
        currentTaskNameView.text = flowService.currentText
        nextTaskNameView.text = flowService.getNextTaskName()
        nextTaskNameView.visibility = View.VISIBLE

    }

    private fun setLongBreakColors() {

        flowParent.background = getDrawable(R.color.longBreakBackgroundColor)
        currentTaskNameView.text = flowService.currentText
        nextTaskNameView.text = flowService.getNextTaskName()
        nextTaskNameView.visibility = View.VISIBLE

    }

}
