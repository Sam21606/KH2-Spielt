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
        setContentView(R.layout.thema_ergebnis)

        init()
    }

    private fun init() {
        DataStore.logQuestionAnswers()
        println("Ich wurde ausgeführt crash?")
        buttonThema = findViewById(R.id.buttonThema)
        textThemaErgebnis = findViewById(R.id.TextThemaErgebnis)
        textThemaErgebnis.text = getString(R.string.topic_text, DataStore.topic)
        buttonThema.setOnClickListener {
        if (DataStore.gameMode) {
            val intent = Intent(this, SpielbrettOnline::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
        }
        }
    }
}