package com.example.ieum

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import com.example.ieum.ui.theme.IeumTheme

class MainActivity : ComponentActivity() {
    private val CHANNEL_ID = "com.example.ieum"
    private var notificationManager: NotificationManager? = null
    private val KEY_REPLY="key_reply"
    inner class WebAppInterface(private val mContext: Context) {
        @JavascriptInterface
        fun test(){}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(CHANNEL_ID,"메시지 알람","this is a test channel")
        var button = findViewById<Button>(R.id.button)
        button.setOnClickListener(){
            displayNotification()
        }
        var web= findViewById<WebView>(R.id.web)
        web.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.setSupportMultipleWindows(true)
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.setSupportZoom(false)
            settings.builtInZoomControls = false
            settings.databaseEnabled = true
            settings.setGeolocationEnabled(true)
            settings.allowFileAccess = true
            // JavaScript 인터페이스 활성화
            addJavascriptInterface(WebAppInterface(this@MainActivity),"WebAppInterface")
        }
        web.loadUrl("https://www.naver.com/")
    }
    private fun displayNotification(){
        //알람 콘텐츠 설정
        val pinId=45
        val notificationId = pinId
        val tapResultIntent = Intent(this, SecondActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,tapResultIntent,PendingIntent.FLAG_IMMUTABLE)


        val notification: Notification = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
            .setContentTitle("Demo Title") // 노티 제목
            .setContentText("This is a demo notification") // 노티 내용
            .setSmallIcon(android.R.drawable.ic_dialog_info) //아이콘이미지
            .setAutoCancel(true) // 사용자가 알림을 탭하면 자동으로 알림을 삭제합니다.
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) //노티클릭시 인텐트작업
            .build()
        /* 3. 알림 표시*///---------------------------------------------------------------------------
        //NotificationManagerCompat.notify()에 전달하는 알림 ID를 저장해야 합니다.
        // 알림을 업데이트하거나 삭제하려면 나중에 필요하기 때문입니다.
        notificationManager?.notify(notificationId, notification) //노티실행
    }
    private fun createNotificationChannel(id:String, name: String, channelDescription: String){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply{
                description=channelDescription

            }
            notificationManager?.createNotificationChannel(channel)
        }else{

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IeumTheme {
        Greeting("Android")
    }
}