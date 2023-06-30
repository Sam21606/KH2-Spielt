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

class BewertungOffline1 : AppCompatActivity() { // KLassennamen camel case

    lateinit var ratingBewertung: RatingBar
    lateinit var buttonWeiterBewertung: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bewertung_offline1)

        init()
    }

    private fun init() {
        ratingBewertung = findViewById<RatingBar>(R.id.ratingBewertung)
        buttonWeiterBewertung = findViewById<Button>(R.id.WeiterButtonBewertung1)
        buttonWeiterBewertung.setOnClickListener {
            bewertungAusleser()
        }
    }

    private fun startIntent() {
        var intent = Intent(this, BewertungOffline2::class.java)
        startActivity(intent)
    }

    private fun bewertungAusleser() {
        DataStore.rating1 = ratingBewertung.rating.toInt()
        println("${DataStore.rating1}")
        if (DataStore.rating1 == 0) {

            // ALERTDIALOG
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
            startIntent()
        }
    }

}