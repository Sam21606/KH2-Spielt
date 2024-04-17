package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class BewertungOnline : AppCompatActivity() {

    lateinit var ratingBewertung: RatingBar
    lateinit var buttonWeiterBewertung: Button
    val db = FirebaseFirestore.getInstance()
    lateinit var textViewInputVonMitspieler: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bewertung_online)

        init()
    }

    private fun init() {
        ratingBewertung = findViewById(R.id.ratingBewertung)
        buttonWeiterBewertung = findViewById(R.id.WeiterButtonBewertung1)
        textViewInputVonMitspieler = findViewById(R.id.textViewInputVonMitspieler)
        buttonWeiterBewertung.setOnClickListener {
            checkBewertung()
        }
    }

    private fun checkBewertung() {
        DataStore.rating1 = ratingBewertung.rating.toInt()
        if (DataStore.rating1 == 0 && buttonWeiterBewertung.text != "Lösung anfragen") {

        } else {
            checkIfThereIsPlayerinput()
        }
    }

    fun addPointsForRating() {
        if (DataStore.player1OR2) {
            val storyText1: MutableMap<String, Any> = hashMapOf(
                "currentpoints2" to DataStore.rating1 + DataStore.currentPoints2,
            )
            db.collection("Games").document(DataStore.gameID)
                .update(storyText1)
        } else {
            val storyText1: MutableMap<String, Any> = hashMapOf(
                "currentpoints1" to DataStore.rating1 + DataStore.currentPoints1,
            )
            db.collection("Games").document(DataStore.gameID)
                .update(storyText1)

        }
    }

    fun checkIfThereIsPlayerinput() {
        if (DataStore.player1OR2) {
            db.collection("Games").document(DataStore.gameID)
                .get()
                .addOnSuccessListener { document ->
                    textViewInputVonMitspieler.text = document.getString("storyText2")
                }
            if (textViewInputVonMitspieler.text != "Noch hat dein Mitspieler keine Lösung eingegeben" && textViewInputVonMitspieler.text != "") {
                addPointsForRating()
                var intent = Intent(this, Spielbrett::class.java)
                startActivity(intent)
            } else if (textViewInputVonMitspieler.text == "") {
                textViewInputVonMitspieler.text = "Noch hat dein Mitspieler keine Lösung eingegeben"
            }
        } else {

            db.collection("Games").document(DataStore.gameID)
                .get()
                .addOnSuccessListener { document ->
                    textViewInputVonMitspieler.text = document.getString("storyText1")
                }
            if (textViewInputVonMitspieler.text != "Noch hat dein Mitspieler keine Lösung eingegeben" && textViewInputVonMitspieler.text != "") {
                addPointsForRating()
                var intent = Intent(this, Spielbrett::class.java)
                startActivity(intent)
            } else if (textViewInputVonMitspieler.text == "") {
                textViewInputVonMitspieler.text = "Noch hat dein Mitspieler keine Lösung eingegeben"
            }
        }
    }
}