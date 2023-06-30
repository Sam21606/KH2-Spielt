package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ZweiteEtappeOnline : AppCompatActivity() {
    lateinit var ediTextStoryInput: EditText
    lateinit var weiterButton: Button
    var db = FirebaseFirestore.getInstance()
    var ediTextInput = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zweite_etappe_online)

        init()
    }

    private fun init() {
        weiterButton = findViewById(R.id.Weiter)
        ediTextStoryInput = findViewById(R.id.ediTextStoryInput)
        weiterButton.setOnClickListener {
            ediTextInput = ediTextStoryInput.text.toString()
            setStoryText()
            val intent = Intent(this, BewertungOnline::class.java)
            startActivity(intent)
        }
    }

    fun setStoryText() {
        if (DataStore.player1OR2) {
            val storyText1: MutableMap<String, Any> = hashMapOf(
                "storyText1" to ediTextInput,
            )
            db.collection("Games").document(DataStore.gameID)
                .update(storyText1)
        } else {
            val storyText1: MutableMap<String, Any> = hashMapOf(
                "storyText2" to ediTextInput,
            )
            db.collection("Games").document(DataStore.gameID)
                .update(storyText1)

        }

    }

}