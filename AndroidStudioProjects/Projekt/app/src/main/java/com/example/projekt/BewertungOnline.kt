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
        DataStore.stage = 2
        ratingBewertung = findViewById(R.id.ratingBewertung)
        buttonWeiterBewertung = findViewById(R.id.WeiterButtonBewertung1)
        textViewInputVonMitspieler = findViewById(R.id.textViewInputVonMitspieler)
        buttonWeiterBewertung.setOnClickListener {
            checkBewertung()
        }
    }

    private fun checkBewertung() {
        DataStore.rating1 = ratingBewertung.rating.toInt()
        println("${DataStore.rating1}")
        if (DataStore.rating1 == 0 && buttonWeiterBewertung.text != "Lösung anfragen") {
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
            checkIfThereIsPlayerinput()
        }
    }

    fun addPointsForRating() {
        if (DataStore.player1OR2) {
            val storyText1: MutableMap<String, Any> = hashMapOf(
                "currentpoints2" to DataStore.rating1,
            )
            db.collection("Games").document(DataStore.gameID)
                .update(storyText1)
        } else {
            val storyText1: MutableMap<String, Any> = hashMapOf(
                "currentpoints1" to DataStore.rating1,
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