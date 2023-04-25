package com.example.ieum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat

class SecondActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
}