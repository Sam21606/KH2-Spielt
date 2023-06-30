package com.example.projekt

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity

class BewertungOffline2 : AppCompatActivity() {
    lateinit var ratingBewertung: RatingBar
    lateinit var buttonWeiterBewertung: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bewertung_offline2)

        init()
    }


    private fun init() {
        DataStore.stage = 2
        ratingBewertung = findViewById<RatingBar>(R.id.ratingBewertung)
        buttonWeiterBewertung = findViewById<Button>(R.id.WeiterButtonBewertung1)
        buttonWeiterBewertung.setOnClickListener {
            chekSetBewertung()
        }
    }

    private fun chekSetBewertung() {
        DataStore.rating2 = ratingBewertung.rating.toInt()
        println("${DataStore.rating2} ${DataStore.rating1}")
        if (DataStore.rating2 == 0) {
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
        } else {
            var intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
        }
    }
}