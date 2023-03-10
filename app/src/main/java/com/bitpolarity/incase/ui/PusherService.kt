package com.bitpolarity.incase.ui

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.bitpolarity.incase.R
import com.bitpolarity.incase.models.Beacon
import com.bitpolarity.incase.ui.lockscreen.LockScreenActivity
import com.bitpolarity.incase.ui.lockscreen.LockScreenViewModel
import com.google.gson.Gson
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import org.json.JSONObject


class PusherService : Service() {

    lateinit var pusher: Pusher
    lateinit var channel : Channel
    lateinit var mediaPlayer: MediaPlayer
    val channelID = "2"
    val gson = Gson()
    lateinit var notificationManager : NotificationManager
    lateinit var viewModel: LockScreenViewModel
    private lateinit var handler: Handler


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    fun updateBeacon(beacon: Beacon){
       handler.post{
           viewModel.setBeacon(beacon)
       }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {



        val options = PusherOptions()
        options.setCluster("ap2")
        pusher = Pusher("276c6bb8aeb296ca6dba", options)
        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.i("Pusher", "State changed from ${change.previousState} to ${change.currentState}")
            }

            override fun onError(
                message: String,
                code: String,
                e: Exception
            ) {
                Toast.makeText(this@PusherService, "\"Pusher :  ($code), message ($message), exception($e)", Toast.LENGTH_SHORT).show()
            }
        }, ConnectionState.ALL)

        Init()
        channel = pusher.subscribe("sexy-channel69")
        showServiceNotification()

        channel.bind("sexy-event"){
                event->
            Log.i("Pusher","Received event with data : $event")
            val dataObj = JSONObject(event.toString())
            val data = JSONObject(dataObj.getString("data")).toString()
            val beacon : Beacon = gson.fromJson(data,Beacon::class.java)

            showBeaconNotification(beacon)

            if (beacon.haveToRing()){
                ring(beacon.priority)
            }

        }

        return START_STICKY
    }

    private fun Init() {

        handler = Handler(mainLooper)
        mediaPlayer = MediaPlayer()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(LockScreenViewModel::class.java)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "my-channel"
            val channelName = "My Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply { lockscreenVisibility = Notification.VISIBILITY_PUBLIC }
            notificationManager.createNotificationChannel(channel)
        }


    }

    private fun ring(tone:Int){

        if (mediaPlayer.isPlaying)
        {
            mediaPlayer.stop()
        }

        if (tone==1){

            mediaPlayer = MediaPlayer.create(this, R.raw.gta)
            mediaPlayer.start()

        }else if (tone == 2) {
            mediaPlayer = MediaPlayer.create(this, R.raw.siren)
            mediaPlayer.start()
        }

    }

    private fun showServiceNotification(){


        val notificationId = 1
        val intent = Intent(this, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(this, "my-channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("InCase")
            .setContentText("Everything is fine")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .build()


        notificationManager.notify(notificationId, notificationBuilder)
        startForeground(1,notificationBuilder)

    }

    private fun showBeaconNotification(beacon: Beacon) {
       // val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channelId = "my-channel"
//            val channelName = "My Channel"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(channelId, channelName, importance).apply { lockscreenVisibility = Notification.VISIBILITY_PUBLIC }
//            notificationManager.createNotificationChannel(channel)
//        }



        //updateBeacon(beacon)
        val fullScreenIntent = Intent(this, LockScreenActivity::class.java)
        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        fullScreenIntent.putExtra("beacon",beacon)
        startActivity(fullScreenIntent)

        val notificationId = 1
        val intent = Intent(this, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(this, "my-channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("InCase: ${beacon.priorityTitle()}")
            .setContentText("${beacon.sender} : ${beacon.message}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .build()


        notificationManager.notify(notificationId, notificationBuilder)



    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    class MyBinder : Binder() {
        val service: PusherService
            get() = PusherService()

    }

}


