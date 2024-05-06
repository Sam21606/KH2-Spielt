package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BoardOffline : AppCompatActivity() {

    private lateinit var textViewBoard: TextView
    private lateinit var buttonSpielbrett: Button
    private lateinit var textViewPlayer1: TextView
    private lateinit var textViewPlayer2 : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board)

        init()
    }

    private fun init() {
        DataStore.player1OR2 = true
        buttonSpielbrett = findViewById(R.id.buttonSpielbrett)
        textViewBoard = findViewById(R.id.textViewBoard)
        textViewPlayer1 = findViewById(R.id.textViewPlayer1)
        textViewPlayer2 = findViewById(R.id.textViewPlayer2)

        textViewBoard.text = getString(R.string.stage , DataStore.stage.toString())
        setPunkteanzeigen()

        buttonSpielbrett.setOnClickListener {
            setNewActivity()
        }
    }

    private fun startIntent(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    private fun setNewActivity() {

        when (DataStore.stage) {
            1 -> {
                startIntent(EventCard::class.java)
            }
            2 -> {
                startIntent(QuizOffline::class.java)
            }
            3 -> {
                startIntent(CultureExperience::class.java)
            }
            4 -> {
                println("${DataStore.stage}")
            }
        }
    }

    private fun setPunkteanzeigen() {
        textViewPlayer1.text = getString(
            R.string.player_points ,
            DataStore.playerName1 ,
            DataStore.currentPoints1.toString()
        )
        textViewPlayer2.text = getString(
            R.string.player_points ,
            DataStore.playerName2 ,
            DataStore.currentPoints2.toString()
        )
    }
}