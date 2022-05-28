package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var resultFileName: String
    private lateinit var statusFile: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)



        resultFileName = intent.getStringExtra("fileName").toString()
        statusFile = intent.getStringExtra("status").toString()
        resultFile.text = resultFileName
        resultStatusText.text = statusFile

        okButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}
