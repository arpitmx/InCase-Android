package com.bitpolarity.incase.models

import com.bitpolarity.incase.R

data class Beacon(val message : String?, val priority : Int, val sender : String){

    fun haveToRing():Boolean{
        return priority>0
    }



    fun priorityTitle():String{
        when(priority){
            0-> return "Low alert"
            1-> return "Medium alert"
            2-> return "High alert"
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



}