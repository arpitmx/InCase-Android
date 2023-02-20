package com.bitpolarity.incase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bitpolarity.incase.databinding.ActivityMainBinding
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stopService(Intent(this, PusherService::class.java))
        val options = PusherOptions()
        options.setCluster("ap2")

        val pusher = Pusher("276c6bb8aeb296ca6dba", options)
        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.i("Pusher", "State changed from ${change.previousState} to ${change.currentState}")
            }

            override fun onError(
                message: String,
                code: String,
                e: Exception
            ) {
                Toast.makeText(this@MainActivity, "\"There was a problem connecting! code ($code), message ($message), exception($e)", Toast.LENGTH_SHORT).show()
            }
        }, ConnectionState.ALL)

        val channel = pusher.subscribe("sexy-channel69")
        channel.bind("sexy-event"){
            event->
            Log.i("Pusher","Received event with data : $event")
            val dataObj = JSONObject(event.toString())
            val data = JSONObject(dataObj.getString("data")).getString("message")
            display(text = data)
        }

    }

    fun display (text: String){
        runOnUiThread {
            binding.text.text = text
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        startService(Intent(this, PusherService::class.java))

    }
}