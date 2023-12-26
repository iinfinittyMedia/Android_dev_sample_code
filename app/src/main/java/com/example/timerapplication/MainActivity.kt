package com.example.timerapplication



import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var textView : TextView
    lateinit var btnStart : Button
    lateinit var btnStop : Button



    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    val yourCountDownTimer = object : CountDownTimer(30000, 1000) {

        // Callback function, fired on regular interval
        override fun onTick(millisUntilFinished: Long) {
            textView.setText("seconds remaining: " + millisUntilFinished / 1000)
        }

        // Callback function, fired
        // when the time is up
        override fun onFinish() {
            textView.setText("done!")
            show();
        }
    };
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        textView = findViewById(R.id.textView)
        btnStart = findViewById(R.id.buttonStart);
        btnStart.setOnClickListener {
            startTimer()
        }

        btnStop = findViewById(R.id.buttonStop);
        btnStop.setOnClickListener {
            yourCountDownTimer.cancel()
        }
        // time count down for 30 seconds,
        // with 1 second as countDown interval

    }


    fun startTimer() {
        yourCountDownTimer.start();
    }

    fun show(){
        Notify.build(getApplicationContext())

            .setTitle("Android Timer")
            .setContent("Android timer timesup!")

            .largeCircularIcon()
            .show(); // Show notification
    }

}


