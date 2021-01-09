package com.example.servicesampleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.servicesampleapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var  binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        initialize()
    }

    private fun initialize(){
        onPlayButton()
        onStopButton()
    }

    private fun onPlayButton(){
        binding.btPlay.setOnClickListener {
            val intent = Intent(applicationContext, SoundManageService::class.java)
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
}