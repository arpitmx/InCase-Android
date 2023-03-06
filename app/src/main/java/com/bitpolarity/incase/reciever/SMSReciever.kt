package com.bitpolarity.incase.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import kotlin.math.log

class SMSReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action == "android.provider.Telephony.SMS_RECEIVED"){
            val bundle = intent.extras
            if (bundle!=null){
                val pdus = bundle["pdus"] as Array<*>
                for (i in pdus.indices) {
                    val smsMessage = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    val messageBody = smsMessage.messageBody
                    val sender = smsMessage.originatingAddress
                    Log.d("SMS1234", "-------\n${messageBody}\n${sender}\n${smsMessage.userData}-------")
                    // Parse the OTP from the messageBody
                    // and set it to the EditText field
                }
            }
        }
    }

}