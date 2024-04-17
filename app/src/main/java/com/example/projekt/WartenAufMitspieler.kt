package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class WartenAufMitspieler : AppCompatActivity() {
    lateinit var buttonWeiterMitspielerWarten: Button
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.warten_auf_mitspieler)

        init()
    }

    private fun init() {
        buttonWeiterMitspielerWarten = findViewById(R.id.buttonWeiterMitspielerWarten)
        buttonWeiterMitspielerWarten.setOnClickListener {
            checkIfNewInput()
        }
    }

    private fun checkIfNewInput() {
        val docRef = db.collection("Games").document(DataStore.gameID)
        docRef.get()
            .addOnSuccessListener { document ->
                DataStore.topic = document.getString("topic").toString()
            }
        getInput()
    }

    private fun getInput() {

        if (DataStore.topic == "Theater" || DataStore.topic == "Oper" || DataStore.topic == "Lesung" || DataStore.topic == "Performance" || DataStore.topic == "Ausstellung" || DataStore.topic == "Konzert") {
            val intent = Intent(this, ThemaErgebnis::class.java)
            startActivity(intent)
        }
    }

}