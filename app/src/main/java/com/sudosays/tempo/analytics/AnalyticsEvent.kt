package com.sudosays.tempo.analytics

import java.time.DateTimeException

/**
 * Created by julius on 2020/06/02.
 */
data class AnalyticsEvent(val timestamp: Long = System.currentTimeMillis(), val type: EventType) {

    enum class EventType {
        FLOW_SESSION_START,
        FLOW_SESSION_INTERRUPT,
        FLOW_SESSION_COMPLETE, //RARE!
        TASK_SESSION_START,
        TASK_SESSION_INTERRUPT,
        TASK_SESSION_COMPLETE,
        TASK_COMPLETE,
        SHORT_BREAK_START,
        SHORT_BREAK_INTERRUPT,
        SHORT_BREAK_COMPLETE,
        LONG_BREAK_START,
        LONG_BREAK_INTERRUPT,
        LONG_BREAK_COMPLETE,
        // TODO: Add goal reaching analytics (later)
    }

}