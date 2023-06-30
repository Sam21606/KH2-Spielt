package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ThemaErgebnis : AppCompatActivity() {

    lateinit var buttonThema: Button
    lateinit var textThemaErgebnis: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thema_ergebniss)

        init()
    }

    private fun init() {
        buttonThema = findViewById(R.id.buttonThema)
        textThemaErgebnis = findViewById(R.id.TextThemaErgebnis)
        textThemaErgebnis.text = getString(R.string.topic_text, DataStore.topic)
        buttonThema.setOnClickListener {
            val intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
        }
        DataStore.addGameDataToFirestore()
    }
}