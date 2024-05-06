package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CultureExperience : AppCompatActivity() {
    private lateinit var buttonStufe4Offline: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.culuture_experinece)

        init()
    }

    private fun init() {
        DataStore.stage = 4
        buttonStufe4Offline = findViewById(R.id.cultureContinue)
        buttonStufe4Offline.setOnClickListener {
            val intent = Intent(this, BoardOffline::class.java)
            startActivity(intent)
        }

    }

}