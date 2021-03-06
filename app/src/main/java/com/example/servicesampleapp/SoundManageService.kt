package com.example.servicesampleapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.io.IOException

private var player: MediaPlayer? = null

class SoundManageService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
        channelBuilder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mediaFileUri = Uri.parse("android.resource://${packageName}/${R.raw.sumple_music}")
        try {
            player?.apply {
                setDataSource(applicationContext, mediaFileUri)
                setOnPreparedListener {
                    it.start()
                    notificationOnStart()
                }
                setOnCompletionListener {
                    notificationOnCompletion()
                    stopSelf()
                }
                prepareAsync()
            }
        }
        catch (ex: IllegalArgumentException){
            Log.e("ServiceSample","メディアプレーヤー準備時の例外",ex)
        }
        catch (ex: IOException) {
            Log.e("ServiceSample", "メディアプレーヤー準備時の例外", ex)
        }
            return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun channelBuilder(){
        val id = "soundmanagerservice_notification_channel"
        val name = getString(R.string.notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun notificationOnStart(){
        val intent = Intent(applicationContext,MainActivity::class.java)
        intent.putExtra(KEY_BOOL, true)
        val stopServiceIntent = PendingIntent.getActivity(applicationContext,0,intent,PendingIntent.FLAG_CANCEL_CURRENT)
        val builder = NotificationCompat.Builder(applicationContext,"soundmanagerservice_notification_channel").apply {
            setSmallIcon(android.R.drawable.ic_dialog_info)
            setContentTitle(getString(R.string.msg_notification_title_start))
            setContentText(getString(R.string.msg_notification_text_start))
            setContentIntent(stopServiceIntent)
            setAutoCancel(true)
        }
        val notification = builder.build()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0,notification)
    }

    private fun notificationOnCompletion(){
        val builder = NotificationCompat.Builder(applicationContext,"soundmanagerservice_notification_channel").apply {
            setSmallIcon(android.R.drawable.ic_dialog_info)
            setContentTitle(getString(R.string.msg_notification_title_finish))
            setContentText(getString(R.string.msg_notification_text_finish))
        }
        val notification = builder.build()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0,notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.let {
            if (it.isPlaying){
                it.stop()
            }
            it.release()
            player = null
        }
    }

    companion object{
        const val KEY_BOOL = "key_bool"
        const val KEY_PLAYING = "key_playing"
    }
}