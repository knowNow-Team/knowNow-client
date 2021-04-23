package com.example.konwnow.ui.view.mypage

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.konwnow.R
import com.example.konwnow.data.model.dto.Words


class AlarmBroadcastReceiver: BroadcastReceiver() {
    companion object {
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 33
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        var count = 0
        val REPLY_KEY = "reply"
        val REPLY_LABEL = "단어 입력" // Action 에 표시되는 Label
    }
    lateinit var notificationManager: NotificationManager
    lateinit var wordsList: ArrayList<Words>

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "Received intent : $intent")
        Log.d(TAG, "Received extra : ${intent!!.extras}")
        var keys = intent!!.extras!!.keySet();
        Log.d(TAG, "------------------------------")
        for (i in keys) {
        Log.d(TAG, i + " : " + intent!!.getBundleExtra("word"))
        }
        Log.d(TAG, "------------------------------")

//        wordsList = intent!!.extras?.getParcelable("word")
        wordsList = intent!!.getBundleExtra("word")!!.getParcelableArrayList<Words>("wordList") as ArrayList<Words>

        if(wordsList!= null){
            notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            createNotificationChannel()
            deliverNotification(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, ReplyReceiver::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val contentPendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        var remoteInput = RemoteInput.Builder(REPLY_KEY)
            .setLabel(REPLY_LABEL)
            .build()

        val notiAction = NotificationCompat.Action.Builder(
            R.drawable.ic_right,
            REPLY_LABEL,
            contentPendingIntent
        )
            .addRemoteInput(remoteInput)
            .setAllowGeneratedReplies(true)
            .build()

        val builder =
            NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_hit)
                .setContentTitle("단어 알림")
                .setContentText(wordsList[count++].kor)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(notiAction)
                .setAutoCancel(false)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Stand up notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.description = "AlarmManager Tests"
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
    }
}