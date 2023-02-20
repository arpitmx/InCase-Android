package com.bitpolarity.incase.models

import android.os.Parcel
import android.os.Parcelable
import com.bitpolarity.incase.R

data class Beacon(val message : String?, val priority : Int, val sender : String) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()!!
    )

    fun haveToRing():Boolean{
        return priority>0
    }


    fun priorityTitle():String{
        when(priority){
            0-> return "Low alert"
            1-> return "Medium alert"
            2-> return "Urgent - Requires immediate attention"
            else-> return "Invalid alert level"
        }
    }

    fun priorityColor():String{
        when(priority){
            0-> return "#FF0090FF"
            1-> return "#FFFF5100"
            2-> return "#FF001E"
            else -> return "#fff"
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeInt(priority)
        parcel.writeString(sender)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Beacon> {
        override fun createFromParcel(parcel: Parcel): Beacon {
            return Beacon(parcel)
        }

        override fun newArray(size: Int): Array<Beacon?> {
            return arrayOfNulls(size)
        }
    }


}