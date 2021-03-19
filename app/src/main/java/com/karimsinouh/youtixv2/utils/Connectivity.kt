package com.karimsinouh.youtixv2.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build

object Connectivity {

    fun hasInternet(c:Context):Boolean{

        val manager=c.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            manager.activeNetwork!=null
        else
            manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.state==NetworkInfo.State.CONNECTED ||
                    manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)?.state== NetworkInfo.State.CONNECTED
    }

}