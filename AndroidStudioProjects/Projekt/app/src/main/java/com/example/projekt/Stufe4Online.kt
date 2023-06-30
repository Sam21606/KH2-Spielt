package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Stufe4Online : AppCompatActivity() {
    lateinit var buttonstufe4Online: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stufe4_online)

        init()
    }

    private fun init() {
        DataStore.stage = 4
        buttonstufe4Online = findViewById<Button>(R.id.buttonStufe4Online)
        buttonstufe4Online.setOnClickListener {
            val intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
        }

    }

}