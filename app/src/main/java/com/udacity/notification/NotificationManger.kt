package com.udacity.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

private const val NOTIFICATION_ID = 100
private const val CHANNEL_ID = "channelId"

@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(
    msg: String,
    applicationContext: Context,
    status: String
) {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.apply {
        putExtra("fileName", msg)
        putExtra("status", status)
    }

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val action =
        NotificationCompat.Action.Builder(0, "Check the status", contentPendingIntent).build()
    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle("Udacity: Android kotlin Nano degree")
        .setContentText(msg)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(action)

    notify(NOTIFICATION_ID, builder.build())
}