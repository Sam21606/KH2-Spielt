package com.example.projekt

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class OnlineVerbindung : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    lateinit var buttonConnectGame: Button
    lateinit var buttonCreateGame: Button
    lateinit var editTextIDInput: EditText
    var buttonCreateGameClicked = 0
    lateinit var clipboardManager: ClipboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.online_verbindung)

        init()
    }

    private fun init() {
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        buttonCreateGame = findViewById(R.id.buttonCreateGame)
        buttonConnectGame = findViewById(R.id.buttonConnectGame)
        editTextIDInput = findViewById(R.id.editTextIDInput)
        buttonCreateGame.setOnClickListener {
            DataStore.gameData = hashMapOf(
                "playerName1" to DataStore.playerName1,
                "gameID" to DataStore.gameID
            )
            DataStore.createGame()
            DataStore.player1OR2 = true// Spieler true wenn er das Game erstellt
            makeGame()
        }
        buttonConnectGame.setOnClickListener {
            if (buttonCreateGameClicked == 0){
                checkIfGameisThere()
                DataStore.player1OR2 = false// Spieler false wenn er dem Game beitritt
            }else{
                val intent = Intent(this, Veranstaltungswahl::class.java)
                startActivity(intent)
            }
        }
    }

    fun makeGame() {
        if (buttonCreateGameClicked == 0) {
            buttonCreateGame.text = "Kopiere ID"
            buttonCreateGameClicked = 1
            buttonConnectGame.visibility = View.INVISIBLE
        } else if (buttonCreateGameClicked == 1) {
            buttonConnectGame.text = "Game Starten"
            buttonCreateGameClicked = 2
            buttonConnectGame.visibility = View.VISIBLE
            val clipData = ClipData.newPlainText("ID", DataStore.gameID)
            clipboardManager.setPrimaryClip(clipData)
        }
    }


    fun checkIfGameisThere() {
        editTextIDInput.visibility = View.VISIBLE
        buttonConnectGame.text = "ID Prüfen"
        db.collection("Games").document(editTextIDInput.text.toString())
            .get()
            .addOnSuccessListener {result ->
                if (result != null){
                    DataStore.gameID = editTextIDInput.text.toString()


                    val intent = Intent(this, Veranstaltungswahl::class.java)
                    startActivity(intent)

                }

            }
    }
}