package com.example.finalexam.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalexam.R
import com.example.finalexam.api.API
import com.example.finalexam.api.Photos
import com.example.finalexam.room.App
import com.example.finalexam.room.Info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class Alarm : Fragment(R.layout.alarm) {

    private lateinit var ivBackground: ImageView
    private lateinit var customAdapter: CustomAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: ImageView

    private val db by lazy { App.instance.db.getMethods() }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivBackground = view.findViewById(R.id.ivBackgroundPhoto)
        btnAdd = view.findViewById(R.id.btnAdd)
        recyclerView = view.findViewById(R.id.recyclerView)

        API().getPhotos().enqueue(object : Callback<Photos> {
            override fun onResponse(call: Call<Photos>, response: Response<Photos>) {
                val body = response.body()
                if (body != null) {
                    val photos = body.photos
                    if (photos.isNotEmpty()) {
                        val randomIndex = Random.nextInt(photos.size)
                        val image = photos[randomIndex].src.portrait

                        Glide.with(requireContext().applicationContext)
                            .asDrawable()
                            .load(image)
                            .into(ivBackground)
                    }
                }
            }

            override fun onFailure(call: Call<Photos>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }
        })

        customAdapter = CustomAdapter ({ info, status ->
            db.updateAlarmStatus(info.id, status)
            refresh()
        },{
            db.deleteAlarm(it)
            refresh()
        })

        recyclerView.adapter = customAdapter

        btnAdd.setOnClickListener {
            val pickerView =
                LayoutInflater.from(requireContext()).inflate(R.layout.add_alarm_dialog, null)
            val timerPicker: TimePicker = pickerView.findViewById(R.id.tp)

            val dialog = AlertDialog.Builder(requireActivity())
            dialog.setTitle("Add Alarm")
            dialog.setView(pickerView)
            dialog.setPositiveButton("დამახსოვრება") { _, _ ->
                val time =
                    "${"%02d".format(timerPicker.hour)}:${"%02d".format(timerPicker.minute)}"
                Info(0, true, time).let { db.insert(it) }
                refresh()
            }.setNegativeButton("გაუქმება") { _, _ ->

            }.show()
            timerPicker.setIs24HourView(true)
        }

        refresh()
    }

    private fun refresh() {
        db.getAlarmsSortedByActiveDesc().let {
            customAdapter.submitList(it)
        }
    }
}
