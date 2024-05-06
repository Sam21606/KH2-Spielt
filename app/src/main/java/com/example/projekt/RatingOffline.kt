package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RatingOffline : AppCompatActivity() { // KLassennamen camel case

    private lateinit var ratingBewertung: RatingBar
    private lateinit var buttonWeiterBewertung: Button
    private lateinit var playerToRateText: TextView
    private var rating1 = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bewertung_offline)

        init()
    }

    private fun init() {
        ratingBewertung = findViewById(R.id.ratingBewertung)
        buttonWeiterBewertung = findViewById(R.id.WeiterButtonBewertung)
        playerToRateText = findViewById(R.id.playerToRate)
        playerToRateText.text = getString(R.string.bewerte_nun , DataStore.playerName2)
        DataStore.player1OR2 = true
        buttonWeiterBewertung.setOnClickListener {
            bewertungAusleser()
        }
        DataStore.chosenPopout = mutableListOf(getString(R.string.no_rating), getString(R.string.no_rating_explained))
    }

    private fun startIntent() {
        val intent = Intent(this, Spielbrett::class.java)
        startActivity(intent)
    }

    private fun bewertungAusleser() {
        rating1 = ratingBewertung.rating.toInt()
        if (rating1 != 0){
            if(DataStore.player1OR2){
                DataStore.currentPoints2 = rating1
                playerToRateText.text = getString(R.string.bewerte_nun2 , DataStore.playerName1)
                rating1 = 0
                DataStore.player1OR2 = false
                ratingBewertung.rating = 0F
            }else{
                DataStore.currentPoints1 = rating1
                DataStore.stage = 2
                startIntent()
            }
        }else{
            DataStore.popout(this)
        }
    }
}