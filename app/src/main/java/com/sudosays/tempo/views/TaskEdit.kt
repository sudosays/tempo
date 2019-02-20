package com.sudosays.tempo.views

import android.content.Context
import android.util.AttributeSet
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.widget.TextView
import com.sudosays.tempo.R
import com.sudosays.tempo.data.Task
import kotlinx.android.synthetic.main.edit_task.view.*

/**
 *
 * Author:
 *
 * Created on: 2018/09/16
 *
 */
class TaskEdit @JvmOverloads constructor(context: Context,
                                         attrs: AttributeSet? = null,
                                         defStyle: Int = 0
                                         //defStyleRes: Int = 0
): ConstraintLayout(context,attrs,defStyle){

    /*var taskTime = 0
    val summaryTemplate:String = "Your activity will take ${taskTime}min"*/

    init {
        LayoutInflater.from(context).inflate(R.layout.edit_task,this,true)
        /*durationEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                summaryTextView.setText("Ha!", TextView.BufferType.NORMAL)
            }

        })*/

    }

    fun populate(task: Task) {
        nameEditText.setText(task.name, TextView.BufferType.EDITABLE)
        durationEditText.setText(task.duration.toString(), TextView.BufferType.EDITABLE)

    }



    /*fun updateSummary(duration:Int) {
        taskTime = duration*25
        summaryTextView.text = "HA!"
    }*/


}