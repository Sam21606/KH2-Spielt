package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EventCard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ereignisskarte)

        init()
    }

    private fun init() {
        val buttonWeiterEreigniskarte = findViewById<Button>(R.id.buttonWeiterEreigniskarte)
        buttonWeiterEreigniskarte.setOnClickListener {
            intent = Intent(this, RatingOffline::class.java)
            startActivity(intent)
        }
    }

}