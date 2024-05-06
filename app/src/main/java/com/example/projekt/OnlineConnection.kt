package com.example.projekt

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class OnlineConnection : AppCompatActivity() {
    private var db = FirebaseFirestore.getInstance()
    private lateinit var buttonConnectGame: Button
    private lateinit var buttonCreateGame: Button
    private lateinit var editTextIDInput: EditText
    private var buttonCreateGameClicked = 0
    private var buttonConnectGameClicked = 0
    private var buttonRejoinGameClicked = 0
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var continueGame : Button
    private lateinit var toggleButtonPlayerNumberInput : ToggleButton

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
        continueGame = findViewById(R.id.continueGame)
        toggleButtonPlayerNumberInput = findViewById(R.id.ToggleButtonPlayerNumberInput)
        buttonCreateGame.setOnClickListener {
            DataStore.player1OR2 = true// Spieler true wenn er das Game erstellt
            makeGame()
        }
        buttonConnectGame.setOnClickListener {
            if (buttonConnectGameClicked == 0 && buttonCreateGameClicked == 1){
                val intent = Intent(this, Veranstaltungswahl::class.java)
                startActivity(intent)
            }else if (buttonConnectGameClicked == 0){
                DataStore.playerName2 = DataStore.playerName1
                DataStore.playerName1 = ""
                buttonConnectGame.text = getString(R.string.checkID)
                buttonCreateGame.visibility = View.INVISIBLE
                editTextIDInput.visibility = View.VISIBLE
                continueGame.visibility = View.INVISIBLE
                buttonConnectGameClicked = 1
            }else if (buttonConnectGameClicked == 1){
                checkIfGameisThere()
                DataStore.player1OR2 = false// Spieler false wenn er dem Game beitritt weil Spieler 2
            }
        }
        continueGame.setOnClickListener {
            checkIfGameExists()
        }
        if (DataStore.reconnect){
            buttonCreateGame.visibility = View.INVISIBLE
            buttonConnectGame.visibility = View.INVISIBLE
            buttonCreateGame.visibility = View.INVISIBLE
            editTextIDInput.visibility = View.VISIBLE
            buttonConnectGame.visibility = View.INVISIBLE
            toggleButtonPlayerNumberInput.visibility = View.VISIBLE
            buttonRejoinGameClicked += 1
            continueGame.text = getString(R.string.checkID)
        }else{
            continueGame.visibility = View.INVISIBLE
        }
    }

    private fun makeGame() {
        if (buttonCreateGameClicked == 0) {
            DataStore.gameData = hashMapOf(
                "playerName1" to DataStore.playerName1,
                "stage" to DataStore.stage,
                "questionCount" to DataStore.questionCount,
                "player1IsReady" to false,
                "player2IsReady" to false,
                "currentPoints1" to 0,
                "currentPoints2" to 0,
                "storyText1" to "Warte auf Eingabe",
                "storyText2" to "Warte auf Eingabe",
                "questionsPicked" to DataStore.questionsPicked,
                "choosenAvatar1" to DataStore.choosenAvatar1
            )
            DataStore.createGame()
            buttonCreateGame.text = getString(R.string.copyID)
            buttonCreateGameClicked = 1
            buttonConnectGame.visibility = View.INVISIBLE
            continueGame.visibility = View.INVISIBLE
        } else if (buttonCreateGameClicked == 1) {
            val clipData = ClipData.newPlainText("ID", DataStore.gameID)
            clipboardManager.setPrimaryClip(clipData)
            buttonConnectGame.visibility = View.VISIBLE
            buttonConnectGame.text= getString(R.string.startGame)
        }
    }


    private fun checkIfGameisThere() {
        if (editTextIDInput.text.length == 20) {
            db.collection("Games").document(editTextIDInput.text.toString())
                .get()
                .addOnFailureListener {
                    DataStore.chosenPopout = mutableListOf(
                        getString(R.string.wrongID) ,
                        getString(R.string.wrongIDExplained)
                    )
                    DataStore.popout(this)
                }
                .addOnSuccessListener { result ->
                    if (result != null) {
                        DataStore.gameID = editTextIDInput.text.toString()
                        DataStore.answer = hashMapOf(
                            "playerName2" to DataStore.playerName2,
                            "choosenAvatar2" to DataStore.choosenAvatar1
                        )
                        DataStore.updateAnswerInDB()
                        val intent = Intent(this, Veranstaltungswahl::class.java)
                        startActivity(intent)
                    }else{
                        DataStore.chosenPopout = mutableListOf(
                            getString(R.string.wrongID) ,
                            getString(R.string.wrongIDExplained)
                        )
                        DataStore.popout(this)
                    }
                }
        }else{
            DataStore.chosenPopout = mutableListOf(
                getString(R.string.wrongID) ,
                getString(R.string.wrongIDExplained)
            )
            DataStore.popout(this)
        }
    }

    private fun checkIfGameExists() {
        DataStore.player1OR2 = toggleButtonPlayerNumberInput.text.toString() == "Player 1" //wenn Player 1 = true
        if (editTextIDInput.text.length == 20) {
            db.collection("Games").document(editTextIDInput.text.toString())
                .get()
                .addOnFailureListener {
                    DataStore.chosenPopout = mutableListOf(
                        getString(R.string.wrongID) ,
                        getString(R.string.wrongIDExplained)
                    )
                    DataStore.popout(this)
                }
                .addOnSuccessListener { result ->
                    if (result != null) {
                        DataStore.gameID = editTextIDInput.text.toString()
                        DataStore.currentPoints1 = result.getLong("currentPoints1")!!.toInt()
                        DataStore.currentPoints2 = result.getLong("currentPoints2")!!.toInt()
                        DataStore.playerName1 = result.getString("playerName1").toString()
                        DataStore.playerName2 = result.getString("playerName2").toString()
                        DataStore.questionCount = result.getLong("questionCount")!!.toInt()
                        val questionsPicked = result.get("questionsPicked")
                        if (questionsPicked is MutableList<*>) {// für denn Fall das nicht alles den Type INT hat
                            DataStore.questionsPicked = questionsPicked.filterIsInstance<Int>().toMutableList()
                        } else{
                            DataStore.questionsPicked = mutableListOf()
                        }
                        DataStore.stage = result.getLong("stage")!!.toInt()
                        val intent = Intent(this , Board::class.java)
                        startActivity(intent)
                    }else{
                        DataStore.chosenPopout = mutableListOf(
                            getString(R.string.wrongID) ,
                            getString(R.string.wrongIDExplained)
                        )
                        DataStore.popout(this)
                    }
                }
        }else{
            DataStore.chosenPopout = mutableListOf(
                getString(R.string.wrongID) ,
                getString(R.string.wrongIDExplained)
            )
            DataStore.popout(this)
        }
    }

}