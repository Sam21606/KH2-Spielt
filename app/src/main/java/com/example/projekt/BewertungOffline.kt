package com.example.projekt

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BewertungOffline : AppCompatActivity() { // KLassennamen camel case

    lateinit var ratingBewertung: RatingBar
    lateinit var buttonWeiterBewertung: Button
    private lateinit var playerToRateText: TextView
    var playerToRate = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bewertung_offline)

        init()
    }

    private fun init() {
        ratingBewertung = findViewById(R.id.ratingBewertung)
        buttonWeiterBewertung = findViewById(R.id.WeiterButtonBewertung1)
        playerToRateText = findViewById(R.id.playerToRate)
        playerToRateText.text = "Bewerte nun ${DataStore.playerName1}"
        buttonWeiterBewertung.setOnClickListener {
            bewertungAusleser()
        }
    }

    private fun startIntent() {
        var intent = Intent(this, Spielbrett::class.java)
        startActivity(intent)
    }

    private fun bewertungAusleser() {
        if (playerToRate == 0){
            DataStore.rating1 = ratingBewertung.rating.toInt()
            playerToRate = 1
            playerToRateText.text = "Bewerte nun ${DataStore.playerName2}"
        }else if (playerToRate == 1){
            DataStore.rating2 = ratingBewertung.rating.toInt()
        }
        if (DataStore.rating1 == 0) {
            popout()
        } else if (DataStore.rating2 == 0) {
            popout()
        }else {
                startIntent()
        }
    }

    private fun popout(){// ALERTDIALOG
        val popoutNoName =
            layoutInflater.inflate(R.layout.popout_keine_bewertung_gegeben, null)
        val popout = Dialog(this)
        popout.setContentView(popoutNoName)
        popout.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popout.window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        popout.window?.attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
        popout.show()
        val popoutButton = popoutNoName.findViewById<Button>(R.id.popoutButton)
        popoutButton.setOnClickListener {
            popout.dismiss()
        }
    }

}