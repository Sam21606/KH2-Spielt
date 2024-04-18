package com.example.projekt

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class SpielbrettOnline : AppCompatActivity(){
    private lateinit var textViewStufe: TextView
    private var pointsShown1 = 0
    private var pointsShown2 = 0
    private var storyText1 = ""
    private var storyText2 = ""
    private var playerCanContinue = false
    private var player1IsReady = false
    private var player2IsReady = false

    private var currentPoints1Before = 0
    private var currentPoints2Before = 0

    private var thereIsTheStoryInput = false

    private lateinit var buttonSpielbrett: Button
    private lateinit var infoButton: ImageButton
    private lateinit var chatButton: ImageButton
    private lateinit var homeButton: ImageButton
    private lateinit var textViewPunkte1: TextView
    private lateinit var textViewPunkte2: TextView

    lateinit var ratingBewertung: RatingBar
    lateinit var buttonWeiterBewertung: Button
    lateinit var textViewInputVonMitspieler: TextView


    var ediTextInput = ""
    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init(){
        DataStore.logQuestionAnswers() // MUSS GEÄNDERT WERDEN
        setSnapshotListener()

        initSpielbrett()
    }

    private fun initSpielbrett() {
        setContentView(R.layout.spielbrett)
        updatePlayerStatusInDB()
        playerCanContinue = false // muss warten bis anderer Spieler bereit ist
        buttonSpielbrett = findViewById(R.id.buttonSpielbrett)
        infoButton = findViewById(R.id.infoButton)
        chatButton = findViewById(R.id.chatButton)
        homeButton = findViewById(R.id.homeButton)
        textViewPunkte1 = findViewById(R.id.textViewPunkte1)
        textViewPunkte2 = findViewById(R.id.textViewPunkte2)
        textViewStufe = findViewById(R.id.textViewStufe)

        textViewStufe.text = "Stufe ${DataStore.stage}"
        setPunkteanzeigen()

        chatButton.setOnClickListener {
            startIntent(Chat::class.java)
        }
        infoButton.setOnClickListener {
            setPunkteanzeigen()
        }
        buttonSpielbrett.setOnClickListener {
            setNewActivity()
        }
        DataStore.currentPoints1 = pointsShown1
        DataStore.currentPoints2 = pointsShown2
    }

    private fun updatePlayerStatusInDB() {
        if (DataStore.player1OR2){
            DataStore.answer = hashMapOf(
                "player1IsReady" to true,
            )
        }else{
            DataStore.answer = hashMapOf(
                "player2IsReady" to true,
            )
        }
        DataStore.updateAnswerInDB()
    }

    private fun setSnapshotListener() {
        db.collection("Games").document(DataStore.gameID)
            .addSnapshotListener{snapshot, _->
                setVarToCompare()

                DataStore.currentPoints1 = snapshot?.getLong("currentPoints1")!!.toInt()
                DataStore.currentPoints2 = snapshot.getLong("currentPoints2")!!.toInt()
                storyText1 = snapshot.get("storyText1").toString()
                storyText2 = snapshot.get("storyText2").toString()
                player1IsReady = snapshot.getBoolean("player1IsReady")!!
                player2IsReady = snapshot.getBoolean("player2IsReady")!!

                checkWhatNewInput()
            }
    }

    private fun setVarToCompare() {
        currentPoints1Before = DataStore.currentPoints1
        currentPoints2Before = DataStore.currentPoints2
    }

    private fun checkWhatNewInput() {
        if (currentPoints2Before != DataStore.currentPoints2 || currentPoints1Before != DataStore.currentPoints1){
            pointsShown1 = DataStore.currentPoints1
            pointsShown2 = DataStore.currentPoints2
        }else if (storyText2 != "" || DataStore.player1OR2){
                thereIsTheStoryInput = true

        }else if (!DataStore.player1OR2 && storyText1 != ""){
            thereIsTheStoryInput = true
        }
        if (player1IsReady || player2IsReady){
            playerCanContinue = true
        }
    }



    private fun startIntent(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }
    fun setPunkteanzeigen() {
        pointsShown1 = DataStore.rating1
        pointsShown2 = DataStore.rating2
        textViewPunkte1.text = pointsShown1.toString()
        textViewPunkte2.text = pointsShown2.toString()
    }

    private fun setNewActivity() {
        DataStore.addGameDataToFirestore()

        if (DataStore.gameMode) {
            when (DataStore.stage) {
                1 -> {
                    setContentView(R.layout.ereignisskarte_offline)
                    initEreignisKarte()
                }
                2 -> {
                    setContentView(R.layout.quiz_online)
                }
                3 -> {
                    setContentView(R.layout.stufe4_online)
                }
                4->{

                }

            }
        }
    }


    // ErignisKarte
    private fun initEreignisKarte() {
        val buttonWeiterEreigniskarte = findViewById<Button>(R.id.buttonWeiterEreigniskarte)
        buttonWeiterEreigniskarte.setOnClickListener {
            setContentView(R.layout.zweite_etappe_online)
            initZweiteEtappe()
        }
    }


    // ZweiteEtappe
    private fun initZweiteEtappe() {
        val weiterButton : Button = findViewById(R.id.Weiter)
        val ediTextStoryInput : TextInputEditText = findViewById(R.id.ediTextStoryInput)

        weiterButton.setOnClickListener {
            ediTextInput = ediTextStoryInput.text.toString()
            setStoryText()
            setContentView(R.layout.bewertung_online)
            initBewertungOnline()
        }
    }

    private fun setStoryText() {
        if (DataStore.player1OR2) {
            DataStore.answer = hashMapOf(
                "storyText1" to ediTextInput,
            )
            DataStore.updateAnswerInDB()
        } else {
            DataStore.answer= hashMapOf(
                "storyText2" to ediTextInput,
            )
            DataStore.updateAnswerInDB()
        }

    }


    // BewertungOnline

    private fun initBewertungOnline() {
         ratingBewertung = findViewById(R.id.ratingBewertung)
         buttonWeiterBewertung = findViewById(R.id.WeiterButtonBewertung1)
         textViewInputVonMitspieler = findViewById(R.id.textViewInputVonMitspieler)
        if (thereIsTheStoryInput){
            if (DataStore.player1OR2){
                textViewInputVonMitspieler.text = storyText2
            } else{
                textViewInputVonMitspieler.text = storyText1
            }
            buttonWeiterBewertung.text!= "Bewertung abgeben"
        }
        buttonWeiterBewertung.setOnClickListener {
            checkBewertung()
        }
    }

    private fun checkBewertung() {
        if (thereIsTheStoryInput) {
            DataStore.rating1 = ratingBewertung.rating.toInt()
            if (DataStore.rating1 == 0) {
                popout()
            } else {
                DataStore.stage = 2
                if (DataStore.player1OR2) {
                    DataStore.answer = hashMapOf(
                        "currentPoints1" to DataStore.currentPoints2,
                    )
                    DataStore.updateAnswerInDB()
                } else {
                    DataStore.answer= hashMapOf(
                        "currentPoints2" to DataStore.currentPoints2,
                    )
                    DataStore.updateAnswerInDB()
                }

                initSpielbrett()
            }
        }
    }


    private fun popout(){
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
    }



    //BEWERTUNG
}