package com.example.projekt

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class OnlineConnection : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    private lateinit var buttonConnectGame: Button
    private lateinit var buttonCreateGame: Button
    private lateinit var editTextIDInput: EditText
    private var buttonCreateGameClicked = 0
    private var buttonConnectGameClicked = 0
    private var buttonRejoinGameClicked = 0
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var continueGame : Button
    private lateinit var ToggleButtonPlayerNumberInput : ToggleButton
    private var chosenPopout : MutableList <String> = mutableListOf() // erste Stelle Titel zweite Stelle Erklärung

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
        ToggleButtonPlayerNumberInput = findViewById(R.id.ToggleButtonPlayerNumberInput)
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
            ToggleButtonPlayerNumberInput.visibility = View.VISIBLE
            buttonRejoinGameClicked += 1
            continueGame.text = getString(R.string.checkID)
        }else{
            continueGame.visibility = View.INVISIBLE
        }
    }

    private fun makeGame() {
        if (buttonCreateGameClicked == 0) {
            DataStore.answer = hashMapOf(
                "playerName1" to DataStore.playerName1,
                "stage" to DataStore.stage
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
                    chosenPopout = mutableListOf(
                        getString(R.string.wrongID) ,
                        getString(R.string.wrongIDExplained)
                    )
                    popout()
                }
                .addOnSuccessListener { result ->
                    if (result != null) {
                        DataStore.gameID = editTextIDInput.text.toString()
                        DataStore.answer = hashMapOf(
                            "playerName2" to DataStore.playerName2,
                            "player1IsReady" to false,
                            "player2IsReady" to false,
                            "currentPoints1" to 0,
                            "currentPoints2" to 0,
                            "storyText1" to "Warte auf Eingabe",
                            "storyText2" to "Warte auf Eingabe"
                        )
                        DataStore.updateAnswerInDB()
                        val intent = Intent(this, Veranstaltungswahl::class.java)
                        startActivity(intent)
                    }else{
                        chosenPopout = mutableListOf(
                            getString(R.string.wrongID) ,
                            getString(R.string.wrongIDExplained)
                        )
                        popout()
                    }
                }
        }else{
            chosenPopout = mutableListOf(
                getString(R.string.wrongID) ,
                getString(R.string.wrongIDExplained)
            )
            popout()
        }
    }

    private fun checkIfGameExists() {
        DataStore.player1OR2 = ToggleButtonPlayerNumberInput.text.toString() == "Player 1"
        if (editTextIDInput.text.length == 20) {
            db.collection("Games").document(editTextIDInput.text.toString())
                .get()
                .addOnFailureListener {
                    chosenPopout = mutableListOf(
                        getString(R.string.wrongID) ,
                        getString(R.string.wrongIDExplained)
                    )
                    popout()
                }
                .addOnSuccessListener { result ->
                    if (result != null) {
                        DataStore.gameID = editTextIDInput.text.toString()
                        DataStore.currentPoints1 = result?.getLong("currentPoints1")!!.toInt()
                        DataStore.currentPoints2 = result.getLong("currentPoints2")!!.toInt()
                        DataStore.playerName1 = result.getString("playerName1").toString()
                        DataStore.playerName2 = result.getString("playerName2").toString()
                        DataStore.questionsPicked =
                            (result.get("questionsPicked") as? MutableList<Int>)!!
                        DataStore.stage = result?.getLong("stage")!!.toInt()
                        val intent = Intent(this , Board::class.java)
                        startActivity(intent)
                    }else{
                        chosenPopout = mutableListOf(
                            getString(R.string.wrongID) ,
                            getString(R.string.wrongIDExplained)
                        )
                        popout()
                    }
                }
        }else{
            chosenPopout = mutableListOf(
                getString(R.string.wrongID) ,
                getString(R.string.wrongIDExplained)
            )
            popout()
        }
    }

    fun popout(){
        val popout = Dialog(this)
        val popoutNoInput = layoutInflater.inflate(R.layout.popout_template, null)
        val popoutText = popoutNoInput.findViewById<TextView>(R.id.popoutText)
        val popoutTitle = popoutNoInput.findViewById<TextView>(R.id.popoutTitle)
        popoutText.text = chosenPopout[1]
        popoutTitle.text = chosenPopout[0]
        popout.setContentView(popoutNoInput)
        popout.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popout.window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        popout.window?.attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
        popout.show()
        val popoutButton = popoutNoInput.findViewById<Button>(R.id.popoutButton)
        popoutButton.setOnClickListener {
            popout.dismiss()
        }
    }
}