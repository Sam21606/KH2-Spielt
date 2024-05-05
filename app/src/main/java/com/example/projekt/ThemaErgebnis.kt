package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ThemaErgebnis : AppCompatActivity() {

    private lateinit var buttonThema: Button
    private lateinit var textThemaErgebnis: TextView
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thema_ergebnis)

        init()
    }

    private fun init() {
        DataStore.logQuestionAnswers()
        buttonThema = findViewById(R.id.buttonThema)
        textThemaErgebnis = findViewById(R.id.TextThemaErgebnis)
        textThemaErgebnis.text = getString(R.string.topic_text, DataStore.topic)
        buttonThema.setOnClickListener {
        if (DataStore.gameMode) {
            db.collection("Games").document(DataStore.gameID)
                .get()
                .addOnSuccessListener {result ->
                    DataStore.playerName2 = result.get("playerName2").toString()
                    DataStore.playerName1 = result.get("playerName1").toString()
                    DataStore.choosenAvatar1 = result.getLong("choosenAvatar1")?.toInt()!!
                    DataStore.choosenAvatar2 = result.getLong("choosenAvatar2")?.toInt()!!
                    val intent = Intent(this, Board::class.java)
                    startActivity(intent)
                }
        }else{
            val intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
        }
        }
    }
}