package com.karimsinouh.youtixv2.utils

import kotlin.math.abs

object ViewsFormatter {

    fun format(number:Int)=when{
        abs(number/1000)>1 -> abs(number/1000).toString()+"k"
        abs(number/1000000)>1 -> abs(number/1000000).toString()+"M"
        abs(number/1000000000)>1 -> abs(number/1000000000).toString()+"B"
        else ->number.toString()
    }

}