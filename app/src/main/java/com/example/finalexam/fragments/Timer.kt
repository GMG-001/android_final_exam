package com.example.finalexam.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.example.finalexam.R
import com.example.finalexam.SharedPreferenceManager
import com.example.finalexam.service.TimerService

class Timer : Fragment(R.layout.timer) {

    var START_MILLI_SECONDS = 60000L

    private lateinit var reset: Button
    private lateinit var button: Button
    private lateinit var timer: TextView
    private lateinit var time_edit_text: EditText
    var isRunning: Boolean = false;
    var time_in_milli_seconds = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reset = view.findViewById(R.id.reset)
        button = view.findViewById(R.id.button)
        timer = view.findViewById(R.id.timer)

        time_edit_text = view.findViewById(R.id.time_edit_text)

        button.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                val time = time_edit_text.text.toString()
                if (time.isEmpty()) {
                    Toast.makeText(context, "Enter Time", Toast.LENGTH_LONG).show()
                } else {
                    if (reset.isInvisible) {
                        time_in_milli_seconds = time.toLong() * 60000L
                    }

                    startTimer(time_in_milli_seconds)
                    TimerService.start(requireContext())
                }
            }
        }
//
        reset.setOnClickListener {
            resetTimer()
        }
    }

    private fun pauseTimer() {
        button.text = "Start"
        TimerService.countdown_timer.cancel()
        isRunning = false
        reset.visibility = View.VISIBLE
    }

    private fun startTimer(time_in_seconds: Long) {
        TimerService.countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                SharedPreferenceManager.instance.setTimer(time_in_milli_seconds)
                updateTextUI()
            }

            override fun onFinish() {
                TimerService.stop(requireActivity())
            }
        }
        TimerService.countdown_timer.start()

        isRunning = true
        button.text = "Pause"
        reset.visibility = View.INVISIBLE
    }

    private fun resetTimer() {
        TimerService.stop(requireContext())
        time_in_milli_seconds = START_MILLI_SECONDS
        updateTextUI()
        reset.visibility = View.INVISIBLE
    }

    private fun updateTextUI() {
        val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60

        timer.text = "$minute:$seconds"
    }

    override fun onResume() {
        super.onResume()

        SharedPreferenceManager.instance.listenTimerUpdates {
            time_in_milli_seconds = it
            updateTextUI()

            isRunning = true
            button.text = "Pause"
            reset.visibility = View.INVISIBLE
        }
    }
}
