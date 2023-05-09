package com.ieumpyo.ieum.geofencing

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ieumpyo.ieum.MainActivity
import com.ieumpyo.ieum.roomdb.notifiedLocationEntity

class NotificationHelper (context: Context): ContextWrapper(context){
    private var notificationManager: NotificationManager? = null
    private val CHANNEL_ID = "com.ieumpyo.ieumpyo"
    private val CHANNEL_NAME = "메시지 알람"

    fun displayNotification(reqId: Int, title: String, body : String, activityName : Class<*>){
//        Log.d(ContentValues.TAG, title+body+"display notification!!")

        //알람 콘텐츠 설정
        val intent = Intent(this, activityName)
//        상태 저장
        val bundle = Bundle()
        bundle.putString("url","http://i-eum-u.com/map/${reqId}")
        bundle.putInt("pin_id",reqId)
//        Log.d("OBSERVER",reqId.toString())
        intent.putExtras(bundle)


        //Activity 중복실행 방지
//        intent.setAction(ACTION_MAIN)
//        intent.addCategory(Intent.CATEGORY_LAUNCHER)
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)


        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title) // 노티 제목
            .setContentText(body) // 노티 내용
            .setSmallIcon(android.R.drawable.ic_dialog_info) //아이콘이미지
            .setAutoCancel(true) // 사용자가 알림을 탭하면 자동으로 알림을 삭제합니다.
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) //노티클릭시 인텐트작업
            .build()
        /* 3. 알림 표시*///---------------------------------------------------------------------------
        //NotificationManagerCompat.notify()에 전달하는 알림 ID를 저장해야 합니다.
        // 알림을 업데이트하거나 삭제하려면 나중에 필요하기 때문입니다.
        notificationManager?.notify(reqId, notification) //노티실행
    }
    fun createNotificationChannel(){
        Log.d(ContentValues.TAG, "create notification!!")

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply{
                description="This is the description of the channel"
                enableLights(true)
                lockscreenVisibility=Notification.VISIBILITY_PUBLIC

            }
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager?.createNotificationChannel(channel)
        }else{

        }
    }
    init{
        createNotificationChannel()
    }
}