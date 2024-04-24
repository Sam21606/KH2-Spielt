package com.example.projekt

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
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
    private var storyText1 = "Warte auf Eingabe"
    private var storyText2 = "Warte auf Eingabe"
    private var playerCanContinue = false
    private var player1IsReady = false
    private var player2IsReady = false

    private var currentPoints1Before = 0
    private var currentPoints2Before = 0

    private var thereIsTheStoryInput = false
    private var ratingIsInitalised = false

    private lateinit var buttonSpielbrett: Button
    private lateinit var infoButton: ImageButton
    private lateinit var chatButton: ImageButton
    private lateinit var homeButton: ImageButton
    private lateinit var textViewPunkte1: TextView
    private lateinit var textViewPunkte2: TextView

    lateinit var ratingBewertung: RatingBar
    lateinit var buttonWeiterBewertung: Button
    lateinit var textViewInputVonMitspieler: TextView

    lateinit var buttonWeiterEreigniskarte : Button

    lateinit var weiterButton : Button
    lateinit var ediTextStoryInput : TextInputEditText


    var ediTextInput = ""
    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSpielbrett()
    }

    private fun initSpielbrett() {
        setContentView(R.layout.spielbrett)
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
        updatePlayerStatusInDB()
        setSnapshotListener()
    }

    private fun updatePlayerStatusInDB() {
        if (DataStore.player1OR2){
            DataStore.answer = hashMapOf(
                "player1IsReady" to true,
            )
            DataStore.updateAnswerInDB()
        }else{
            DataStore.answer = hashMapOf(
                "player2IsReady" to true,
            )
            DataStore.updateAnswerInDB()
        }
    }

    private fun setSnapshotListener() {
        println("DB Funktion SnapshotListener angefangen")
        db.collection("Games").document(DataStore.gameID)
            .addSnapshotListener{snapshot, exception->
                if (exception != null){
                    println("DB Funktion SnapshotListener FAIL")
                }
                setVarToCompare()

                DataStore.currentPoints1 = snapshot?.getLong("currentPoints1")!!.toInt()
                DataStore.currentPoints2 = snapshot.getLong("currentPoints2")!!.toInt()
                storyText1 = snapshot.get("storyText1").toString()
                storyText2 = snapshot.get("storyText2").toString()
                player1IsReady = snapshot.getBoolean("player1IsReady")!!
                player2IsReady = snapshot.getBoolean("player2IsReady")!!



                checkWhatNewInput()
                println("DB Funktion SnapshotListener beendet")
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
        }else if (storyText2 != "Warte auf Eingabe" && DataStore.player1OR2 && ratingIsInitalised ){
            thereIsTheStoryInput = true
            setDisplayedText()
            println("ich wurde ausgeführt2 storyText2 $storyText2  storyText1 $storyText1 ${DataStore.player1OR2} $ratingIsInitalised")
        }else if (!DataStore.player1OR2 && storyText1 != "Warte auf Eingabe" && ratingIsInitalised){
            thereIsTheStoryInput = true
            setDisplayedText()
            println("ich wurde ausgeführt2 storyText2 $storyText2  storyText1 $storyText1 ${DataStore.player1OR2} $ratingIsInitalised")
        }else if (storyText2 != "Warte auf Eingabe" && DataStore.player1OR2){
            thereIsTheStoryInput = true
        }else if (storyText1 != "Warte auf Eingabe" && !DataStore.player1OR2){
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
        textViewPunkte1.text = DataStore.currentPoints1.toString()
        textViewPunkte2.text = DataStore.currentPoints2.toString()
    }

    private fun setNewActivity() {

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
        buttonWeiterEreigniskarte = findViewById<Button>(R.id.buttonWeiterEreigniskarte)
        buttonWeiterEreigniskarte.setOnClickListener {
            initZweiteEtappe()
        }
    }


    // ZweiteEtappe
    private fun initZweiteEtappe() {
        setContentView(R.layout.zweite_etappe_online)
        weiterButton = findViewById(R.id.Weiter)
        ediTextStoryInput = findViewById(R.id.ediTextStoryInput)

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
        buttonWeiterBewertung = findViewById(R.id.WeiterButtonBewertung)
        textViewInputVonMitspieler = findViewById(R.id.textViewInputVonMitspieler)
        ratingIsInitalised = true
        if (thereIsTheStoryInput){
            setDisplayedText()
        }else{
            buttonWeiterBewertung.visibility = View.INVISIBLE
        }
        buttonWeiterBewertung.setOnClickListener {
            checkBewertung()
        }
    }

    private fun checkBewertung() {
        val pointsFromRating = ratingBewertung.rating.toInt()
        if (DataStore.rating1 == 0) {
            popout()
        } else {
            DataStore.stage = 2
            if (DataStore.player1OR2) {
                DataStore.answer = hashMapOf(
                    "currentPoints1" to DataStore.currentPoints2 + pointsFromRating ,
                )
                DataStore.updateAnswerInDB()
            } else {
                DataStore.answer = hashMapOf(
                    "currentPoints2" to DataStore.currentPoints2 + pointsFromRating,
                )
                DataStore.updateAnswerInDB()
            }
            initSpielbrett()
        }
    }

    private fun setDisplayedText(){
        if (DataStore.player1OR2){
            textViewInputVonMitspieler.text = storyText2
        }else{
            textViewInputVonMitspieler.text = storyText1
        }
        buttonWeiterBewertung.visibility = View.VISIBLE
        buttonWeiterBewertung.text = "Bewertung abgeben"
        println("Ich wurde ausgeführt")
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

}