package com.karimsinouh.youtixv2.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.utils.NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    fun notificationManager(@ApplicationContext c:Context)=c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun notificationBuilder(@ApplicationContext c:Context) =
        NotificationCompat.Builder(c, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)


}