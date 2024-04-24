package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Spielbrett : AppCompatActivity() {

    lateinit var textViewStufe: TextView
    var punkteanzeige1 = 0
    var punkteanzeige2 = 0
    lateinit var buttonSpielbrett: Button
    lateinit var infoButton: ImageButton
    lateinit var chatButton: ImageButton
    lateinit var homeButton: ImageButton
    lateinit var textViewPunkte1: TextView
    lateinit var textViewPunkte2: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.spielbrett)

        init()
    }

    private fun init() {
        buttonSpielbrett = findViewById(R.id.buttonSpielbrett)
        infoButton = findViewById(R.id.infoButton)
        chatButton = findViewById(R.id.chatButton)
        homeButton = findViewById(R.id.homeButton)
        textViewPunkte1 = findViewById(R.id.textViewPunkte1)
        textViewPunkte2 = findViewById(R.id.textViewPunkte2)
        textViewStufe = findViewById(R.id.textViewStufe)

        textViewStufe.text = "Stufe ${DataStore.stage}"
        setPunkteanzeigen()

        infoButton.setOnClickListener {
            startIntent(Info::class.java)
        }
        chatButton.setOnClickListener {
            startIntent(Chat::class.java)
        }
        homeButton.setOnClickListener {
            startIntent(PlayerConfig::class.java)

        }
        infoButton.setOnClickListener {
            setPunkteanzeigen()
        }
        buttonSpielbrett.setOnClickListener {
            setNewActivity()
            println("${DataStore.stage}")
        }
        DataStore.currentPoints1 = punkteanzeige1
        DataStore.currentPoints2 = punkteanzeige2
    }

    private fun startIntent(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    private fun setNewActivity() {

        if (DataStore.gameMode) {
            when (DataStore.stage) {
                1 -> {
                    val intent = Intent(this, ZweiteEtappeOnline::class.java)
                    startActivity(intent)
                }

                else -> Unit
            }
        }

        if (DataStore.stage == 1 && DataStore.gameMode) {
            val intent = Intent(this, ZweiteEtappeOnline::class.java)
            startActivity(intent)
        } else if (DataStore.stage == 1 && !DataStore.gameMode) {
            val intent = Intent(this, EreignisskarteOffline::class.java)
            startActivity(intent)
        } else if (DataStore.stage == 2 && DataStore.gameMode) {
            val intent = Intent(this, QuizOnline::class.java)
            startActivity(intent)
        } else if (DataStore.stage == 2 && !DataStore.gameMode) {
            val intent = Intent(this, QuizOffline::class.java)
            startActivity(intent)
        } else if (DataStore.stage == 3 && DataStore.gameMode) {
            val intent = Intent(this, Stufe4Online::class.java)
            startActivity(intent)
        } else if (DataStore.stage == 3 && !DataStore.gameMode) {
            val intent = Intent(this, Stufe4Offline::class.java)
            startActivity(intent)
        } else if (DataStore.stage == 4) {
            println("${DataStore.stage}")
        }
    }

    fun setPunkteanzeigen() {
        punkteanzeige1 = DataStore.rating1
        punkteanzeige2 = DataStore.rating2
        textViewPunkte1.text = punkteanzeige1.toString()
        textViewPunkte2.text = punkteanzeige2.toString()
    }
}