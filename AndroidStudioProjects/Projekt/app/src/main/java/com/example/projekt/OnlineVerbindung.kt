package com.example.projekt

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class OnlineVerbindung : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    lateinit var buttonConnectGame: Button
    lateinit var textViewIDContainer: TextView
    lateinit var buttonCreateGame: Button
    lateinit var editTextIDInput: EditText
    var buttonCreateGameClicked = 0
    var isEmpty = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.online_verbindung)

        init()
    }

    private fun init() {
        textViewIDContainer = findViewById(R.id.textViewIDContainer)
        buttonCreateGame = findViewById(R.id.buttonCreateGame)
        buttonConnectGame = findViewById(R.id.buttonConnectGame)
        editTextIDInput = findViewById(R.id.editTextIDInput)
        buttonCreateGame.setOnClickListener {
            DataStore.player1OR2 = true// Spieler true wenn er das Game erstellt
            makeGame()
        }
        buttonConnectGame.setOnClickListener {
            checkIfGameisThere()
            DataStore.player1OR2 = false// Spieler false wenn er dem Game beitritt
        }
    }

    fun makeGame() {
        if (buttonCreateGameClicked == 0) {
            savePlayer1DataToFirestore()
            buttonCreateGame.text = "ID anzeigen"
            buttonCreateGameClicked = 1
            buttonConnectGame.visibility = View.INVISIBLE
        } else if (buttonCreateGameClicked == 1) {
            TextChangerWithDelay()
            SaveGameIDToGame()
            buttonCreateGame.text = "Game Starten"
            buttonCreateGameClicked = 2
        } else if (buttonCreateGameClicked == 2) {
            val intent = Intent(this, Veranstaltungswahl::class.java)
            startActivity(intent)
        }
    }

    fun checkIfGameisThere() {

        if (buttonCreateGameClicked == 0) {
            editTextIDInput.visibility = View.VISIBLE
            db.collection("Games").whereEqualTo("GameID", editTextIDInput.text.toString())
                .limit(1).get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        isEmpty = task.result.isEmpty
                        println(isEmpty)
                    }
                })
            if (!isEmpty) {
                DataStore.gameID = editTextIDInput.text.toString()
                val intent = Intent(this, Veranstaltungswahl::class.java)
                startActivity(intent)

            } else {
                println("FEHLER")
            }
        }
    }


    fun savePlayer1DataToFirestore() {
        val games: MutableMap<String, Any> = hashMapOf(
        )
        db.collection("Games")
            .add(games)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, documentReference.id)
                DataStore.gameID = documentReference.id
            }

    }

    fun SaveGameIDToGame() {
        val gameID: MutableMap<String, Any> = hashMapOf(
            "GameID" to DataStore.gameID
        )
        db.collection("Games").document(DataStore.gameID)
            .update(gameID)
    }

    fun TextChangerWithDelay() {
        textViewIDContainer.text = DataStore.gameID
    }

}