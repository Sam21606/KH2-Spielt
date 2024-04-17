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


class OnlineConnection : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    private lateinit var buttonConnectGame: Button
    private lateinit var buttonCreateGame: Button
    private lateinit var editTextIDInput: EditText
    private var buttonCreateGameClicked = 0
    private var buttonConnectGameClicked = 0
    private lateinit var clipboardManager: ClipboardManager

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
            DataStore.player1OR2 = true// Spieler true wenn er das Game erstellt
            makeGame()
        }
        buttonConnectGame.setOnClickListener {
            if (buttonConnectGameClicked == 0 && buttonCreateGameClicked == 1){
                val intent = Intent(this, Veranstaltungswahl::class.java)
                startActivity(intent)
            }else if (buttonConnectGameClicked == 0){
                buttonConnectGame.text = "ID Prüfen"
                buttonCreateGame.visibility = View.INVISIBLE
                editTextIDInput.visibility = View.VISIBLE
                buttonConnectGameClicked = 1
            }else if (buttonConnectGameClicked == 1){
                checkIfGameisThere()
                DataStore.player1OR2 = false// Spieler false wenn er dem Game beitritt weil Spieler 2
            }
        }
    }

    fun makeGame() {
        if (buttonCreateGameClicked == 0) {
            DataStore.answer = hashMapOf(
                "playerName1" to DataStore.playerName1,
                "satge" to DataStore.stage
            )
            DataStore.createGame()
            buttonCreateGame.text = "Kopiere ID"
            buttonCreateGameClicked = 1
            buttonConnectGame.visibility = View.INVISIBLE
        } else if (buttonCreateGameClicked == 1) {
            val clipData = ClipData.newPlainText("ID", DataStore.gameID)
            clipboardManager.setPrimaryClip(clipData)
            buttonConnectGame.visibility = View.VISIBLE
            buttonConnectGame.text= "Game Starten"
            println("Was ist mit der Id ${DataStore.gameID}")
        }
    }


    fun checkIfGameisThere() {
        db.collection("Games").document(editTextIDInput.text.toString())
            .get()
            .addOnSuccessListener {result ->
                if (result != null){
                    DataStore.gameID = editTextIDInput.text.toString()
                    DataStore.answer = hashMapOf(
                        "playerName2" to DataStore.playerName2,
                        "player1IsReady" to false,
                        "player2IsReady" to false,
                        "currentPoints1" to 0,
                        "currentPoints2" to 0
                    )
                    DataStore.updateAnswerInDB()

                    val intent = Intent(this, Veranstaltungswahl::class.java)
                    startActivity(intent)

                }

            }
    }
}