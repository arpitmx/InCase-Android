package com.bitpolarity.incase.ui.lockscreen

import android.app.KeyguardManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.bitpolarity.incase.databinding.ActivityMain2Binding
import com.bitpolarity.incase.models.Beacon

class LockScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var beacon : Beacon
    private lateinit var viewModel: LockScreenViewModel


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        showWhenLockedAndTurnScreenOn()
        actionBar?.hide()
       // val controller = window.insetsController
      //  controller?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())

        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LockScreenViewModel::class.java)


        beacon = intent.getParcelableExtra("beacon")!!
        setView(beacon)

    }

    private fun setView(beacon: Beacon){

//        viewModel.beacon.observe(this) {
//            with(binding) {
//                alerttext.text = beacon.message
//                alertLevel.text = beacon.priorityTitle()
//                senderName.text = beacon.sender
//                mainLayout.setBackgroundColor(Color.parseColor(beacon.priorityColor()))
//            }
//        }

        with(binding){
            alerttext.text = beacon.message
            alertLevel.text = beacon.priorityTitle()
            senderName.text = beacon.sender
            mainLayout.setBackgroundColor(Color.parseColor(beacon.priorityColor()))
        }


    }

    override fun onPause() {
        super.onPause()

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun showWhenLockedAndTurnScreenOn() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

}