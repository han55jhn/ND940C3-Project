package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.notification.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private var path: String? = null
    private var fileName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        ifClickedRadioButton()
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        custom_button.setOnClickListener {
            custom_button.buttonState = ButtonState.Loading
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val action = intent?.action

            if (downloadID == id) {
                if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    val query = DownloadManager.Query()
                    query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
                    val manager =
                        context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val cursor: Cursor = manager.query(query)
                    if (cursor.moveToFirst()) {
                        if (cursor.count > 0) {
                            val status =
                                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                custom_button.buttonState = ButtonState.Completed
                                notificationManager.sendNotification(
                                    fileName.toString(),
                                    applicationContext,
                                    "Success"
                                )
                            } else {
                                custom_button.buttonState = ButtonState.Completed
                                notificationManager.sendNotification(
                                    fileName.toString(),
                                    applicationContext,
                                    "Fail"
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    fun ifClickedRadioButton() {

        download_radio_group.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.retrofit_radio_button -> {
                    fileName = resources.getString(R.string.third_radio)
                    path = "https://github.com/square/retrofit"
                }
                R.id.udacity_radio_button -> {
                    fileName = resources.getString(R.string.second_radio)
                    path =
                        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
                }
                R.id.glide_radio_button -> {
                    fileName = resources.getString(R.string.first_radio)
                    path = "https://github.com/bumptech/glide"
                }
            }
        }

    }

    private fun download() {


        if (path != null) {
            notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
            createNotificationChannel()

            val file = File(getExternalFilesDir(null), "/repos")

            if (!file.exists()) {
                file.mkdirs()
            }

            val request =
                DownloadManager.Request(Uri.parse(URL))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.


        } else {
            custom_button.buttonState = ButtonState.Completed
            Toast.makeText(this, "Please Select the file to download", Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
        private const val CHANNEL_NAME = "LoadApp"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download complete"

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
