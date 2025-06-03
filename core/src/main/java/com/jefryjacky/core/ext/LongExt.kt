package com.jefryjacky.core.ext

fun Long.toCountDownDisplay():String{
    val seconds  = this / 1000
    val minutes = seconds/60
    val remainingSecond = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSecond)
}