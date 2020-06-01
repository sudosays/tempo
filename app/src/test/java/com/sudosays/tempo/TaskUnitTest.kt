package com.sudosays.tempo

import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.sudosays.tempo.data.Task

/**
 * Created by julius on 2020/06/01.
 */
class TaskUnitTest {

    @Test
    fun instantiationValidationCorrectSimpleReturnsTrue() {

        val tempTask = Task(0,"test", 0,0)

        assertThat(tempTask.name).isEqualTo("test")

    }
}