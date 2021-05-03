package com.karimsinouh.youtixv2.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.karimsinouh.youtixv2.ui.main.MainActivity
import com.karimsinouh.youtixv2.utils.NOTIFICATION_CHANNEL_ID
import com.karimsinouh.youtixv2.utils.VIDEO_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random


@AndroidEntryPoint
class NotificationsReceiver:FirebaseMessagingService() {

    @Inject lateinit var manager:NotificationManager
    @Inject lateinit var notification:NotificationCompat.Builder

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()

        val data =message.data

        Glide.with(this).asBitmap().load(data["thumbnail"]).into(object :CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                notification
                        .setContentTitle(data["title"])
                        .setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource) )
                        .setContentIntent(buildPendingIntent(data["videoId"].toString()))


                val randomId= Random.nextInt()
                manager.notify(randomId,notification.build())

            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }

        })

    }

    private fun buildPendingIntent(videoId:String):PendingIntent{
        val intent=Intent(this,MainActivity::class.java).also {
            it.action="notification"
            it.putExtra(VIDEO_ID,videoId)
        }
        return PendingIntent.getActivity(this,0,intent, FLAG_UPDATE_CURRENT)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){

        val channel=NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notifications channel",IMPORTANCE_HIGH).apply {
            description="Here you receive notifications"
        }

        manager.createNotificationChannel(channel)
    }

}