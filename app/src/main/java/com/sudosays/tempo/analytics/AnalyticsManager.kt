package com.sudosays.tempo.analytics


/**
 * Created by julius on 2020/06/02.
 */
class AnalyticsManager {

    private var events: MutableList<AnalyticsEvent> = mutableListOf<AnalyticsEvent>()

    fun log(timestamp: Long = System.currentTimeMillis(), type: AnalyticsEvent.EventType) {


        val event = AnalyticsEvent(timestamp, type)
        events.add(event)

    }

    fun getTaskSessionsCompleted() : Int {

        //TODO: Implementation

        return -1

    }

    fun getTotalTaskTime(): Long {

        // TODO: Implementation

        return -1
    }

    fun getTotalWastedTaskTime(): Long {

        // TODO: Implementation

        return -1

    }

}