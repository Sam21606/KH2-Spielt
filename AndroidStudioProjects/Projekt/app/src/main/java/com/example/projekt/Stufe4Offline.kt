package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Stufe4Offline : AppCompatActivity() {
    lateinit var buttonStufe4Offline: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stufe4_offline)

        init()
    }

    private fun init() {
        DataStore.stage = 4
        buttonStufe4Offline = findViewById<Button>(R.id.buttonStufe4Offline)
        buttonStufe4Offline.setOnClickListener {
            val intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
        }

    }

}