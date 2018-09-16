package com.sudosays.torro

import android.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val t = Task("Say Hello", 1.0f)

        singleTask.populate(t)

    }

    fun changeTaskInfo(view: View) {

        val t = Task("This has changed", 0.75f)

        //singleTask.populate(t)

        val testView = TaskView(this)

        testView.id = 1

        container.addView(testView)

        var constraintSet = ConstraintSet()

        constraintSet.clone(container)

        constraintSet.connect(testView.id, ConstraintSet.TOP, singleTask.id, ConstraintSet.BOTTOM,8)
        constraintSet.connect(testView.id, ConstraintSet.START, container.id, ConstraintSet.START,16)
        constraintSet.connect(testView.id, ConstraintSet.END, container.id, ConstraintSet.END,16)
        constraintSet.setTranslationZ(testView.id,2.0f)
        constraintSet.applyTo(container)

        testView.populate(t)

        Toast.makeText(this,"Created a new task view!",Toast.LENGTH_SHORT).show()

    }

}
