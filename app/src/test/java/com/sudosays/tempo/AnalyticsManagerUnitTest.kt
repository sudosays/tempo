package com.sudosays.tempo

import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.sudosays.tempo.analytics.*

/**
 * Created by julius on 2020/06/02.
 */
class AnalyticsManagerUnitTest {

    @Test
    fun `check report get task sessions completed simple`() {

        val expectedTaskSessionsCompleted = 3
        val testManager = AnalyticsManager()

        // 1
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_START)
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_COMPLETE)

        // 2
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_START)
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_COMPLETE)

        // 3
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_START)
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_COMPLETE)

        assertThat(testManager.getTaskSessionsCompleted()).isEqualTo(expectedTaskSessionsCompleted)

    }

    @Test
    fun `check report get task sessions completed mismatched`() {
        val expectedTaskSessionsCompleted = 1
        val testManager = AnalyticsManager()

        // mismatched! Task cannot be completed before it starts
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_COMPLETE)
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_START)

        // fine should be counted
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_START)
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_COMPLETE)

        // mismatched again should not be counted
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_COMPLETE)
        testManager.log(type = AnalyticsEvent.EventType.TASK_SESSION_START)

        assertThat(testManager.getTaskSessionsCompleted()).isEqualTo(expectedTaskSessionsCompleted)

    }

    @Test
    fun `check report get total task time single task`() {

        var cumulativeTime:Long = 0
        var currentTime = System.currentTimeMillis()

        val testManager = AnalyticsManager()

        testManager.log(timestamp = currentTime, type = AnalyticsEvent.EventType.TASK_SESSION_START)

        val newTime = System.currentTimeMillis()
        cumulativeTime += newTime - currentTime
        currentTime = newTime

        testManager.log(timestamp = currentTime, type = AnalyticsEvent.EventType.TASK_SESSION_COMPLETE)

        assertThat(testManager.getTotalTaskTime()).isEqualTo(cumulativeTime)

    }

    @Test
    fun `check report get total task time multiple task`() {

        var cumulativeTime:Long = 0
        var currentTime = System.currentTimeMillis()

        val testManager = AnalyticsManager()

        for (i in 0..5) {
            testManager.log(timestamp = currentTime, type = AnalyticsEvent.EventType.TASK_SESSION_START)

            val newTime = System.currentTimeMillis()
            cumulativeTime += newTime - currentTime
            currentTime = newTime

            testManager.log(timestamp = currentTime, type = AnalyticsEvent.EventType.TASK_SESSION_COMPLETE)
        }

        assertThat(testManager.getTotalTaskTime()).isEqualTo(cumulativeTime)

    }

    @Test
    fun `check report get total wasted task time single task`() {

        var cumulativeTime:Long = 0
        var currentTime = System.currentTimeMillis()

        val testManager = AnalyticsManager()

        testManager.log(timestamp = currentTime, type = AnalyticsEvent.EventType.TASK_SESSION_START)

        val newTime = System.currentTimeMillis()
        cumulativeTime += newTime - currentTime
        currentTime = newTime

        testManager.log(timestamp = currentTime, type = AnalyticsEvent.EventType.TASK_SESSION_INTERRUPT)

        assertThat(testManager.getTotalWastedTaskTime()).isEqualTo(cumulativeTime)

    }


}