package com.bitpolarity.incase.ui.lockscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bitpolarity.incase.models.Beacon

class LockScreenViewModel : ViewModel() {

    val beacon = MutableLiveData<Beacon>()

    fun setBeacon(b: Beacon){
        beacon.value = b
    }

}