package com.example.servicesampleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.servicesampleapp.databinding.ActivityMainBinding

lateinit var  binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        initialize()
    }

    private fun initialize(){
        onPlayButton()
        onStopButton()
        onStartFromNotification()
    }

    private fun onPlayButton(){
        val intent = Intent(applicationContext, SoundManageService::class.java)
        binding.btPlay.setOnClickListener {
            startService(intent)
            binding.apply {
                btPlay.isEnabled = false
                btStop.isEnabled = true
            }
        }
    }
    private fun onStopButton(){
        binding.btStop.setOnClickListener {
            val intent = Intent(applicationContext, SoundManageService::class.java)
            stopService(intent)
            binding.apply {
                btPlay.isEnabled = true
                btStop.isEnabled = false
            }
        }
    }
    private fun onStartFromNotification(){
        val fromNotification = intent.getBooleanExtra(SoundManageService.KEY_BOOL, false)
        Log.d("onStartFromNotification",fromNotification.toString())

        if (fromNotification)
            binding.apply {
                btPlay.isEnabled = false
                btStop.isEnabled = true
            }
    }


}