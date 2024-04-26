package com.example.projekt

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class Board : AppCompatActivity(){
    private lateinit var textViewStufe: TextView
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
    private lateinit var textViewPlayer1: TextView
    private lateinit var textViewPlayer2 : TextView

    private lateinit var ratingBewertung: RatingBar
    private lateinit var buttonWeiterBewertung: Button
    private lateinit var textViewInputVonMitspieler: TextView

    lateinit var buttonWeiterEreigniskarte : Button

    private lateinit var weiterButton : Button
    private lateinit var ediTextStoryInput : TextInputEditText


    private var ediTextInput = ""
    var db = FirebaseFirestore.getInstance()
    private var chosenPopout : MutableList <String> = mutableListOf() // erste Stelle Titel zweite Stelle Erklärung



    // QUIZ
    private lateinit var quizWeiterOnline: Button
    private lateinit var buttonAnswer1Online: ToggleButton // inizieert die einzelnen Buttons sodass sie im code aufrufbar sind aber noch keinen Value besitzten
    private lateinit var buttonAnswer2Online: ToggleButton
    private lateinit var buttonAnswer3Online: ToggleButton
    private lateinit var buttonAnswer4Online: ToggleButton
    private lateinit var textViewFrageQuiz: TextView
    private var questionNumber = 0
    private var choosedQuestion = 3//Spieler gewählte Anzahl an Fragen
    private var questionID: String = ""
    private var clickedAnswerID = "0"
    private lateinit var questionText: String
    private var answer1Text: String? = null
    private lateinit var answer2Text: String
    private lateinit var answer3Text: String
    private lateinit var answer4Text: String
    private var questionsChosen = mutableListOf("")
    private var matchedAnswers = DataStore.answers.filter { it._QuestionID == questionID }
    private var matchAnswersText = matchedAnswers.map { it.text }
    private var matchAnswersId = matchedAnswers.map { it.ID }
    private var choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID }
    private var correctAnswer = matchedAnswers.filter { it.correct == "true" }
    private var toggleButtonClicked = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBoard()
    }

    private fun initBoard() {
        setContentView(R.layout.board)
        playerCanContinue = false // muss warten bis anderer Spieler bereit ist
        buttonSpielbrett = findViewById(R.id.buttonSpielbrett)
        textViewStufe = findViewById(R.id.textViewBoard)
        textViewPlayer1 = findViewById(R.id.textViewPlayer1)
        textViewPlayer2 = findViewById(R.id.textViewPlayer2)
        textViewStufe.text = getString(R.string.Stage_text , DataStore.stage.toString())
            setPunkteanzeigen()
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
                "stage" to DataStore.stage
            )
            DataStore.updateAnswerInDB()
        }else{
            DataStore.answer = hashMapOf(
                "player2IsReady" to true,
                "stage" to DataStore.stage
            )
            DataStore.updateAnswerInDB()
        }
    }

    private fun setSnapshotListener() {
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
                DataStore.questionsPicked = (snapshot.get("questionsPicked") as? MutableList<Int>)!!



                checkWhatNewInput()
            }
    }

    private fun setVarToCompare() {
        currentPoints1Before = DataStore.currentPoints1
        currentPoints2Before = DataStore.currentPoints2
    }

    private fun checkWhatNewInput() {
        if (currentPoints2Before != DataStore.currentPoints2 || currentPoints1Before != DataStore.currentPoints1){
            setPunkteanzeigen()
        }else if (storyText2 != "Warte auf Eingabe" && DataStore.player1OR2 && ratingIsInitalised ){
            thereIsTheStoryInput = true
            setDisplayedText()
        }else if (!DataStore.player1OR2 && storyText1 != "Warte auf Eingabe" && ratingIsInitalised){
            thereIsTheStoryInput = true
            setDisplayedText()
        }else if (storyText2 != "Warte auf Eingabe" && DataStore.player1OR2){
            thereIsTheStoryInput = true
        }else if (storyText1 != "Warte auf Eingabe" && !DataStore.player1OR2){
            thereIsTheStoryInput = true
        }
        if (player1IsReady || player2IsReady){
            playerCanContinue = true
        }
    }

    fun setPunkteanzeigen() {
        textViewPlayer1.text = getString(
            R.string.player1_points ,
            DataStore.playerName1 ,
            DataStore.currentPoints1.toString()
        )
        textViewPlayer2.text = getString(
            R.string.palyer2_points ,
            DataStore.playerName1 ,
            DataStore.currentPoints2.toString()
        )
    }

    private fun setNewActivity() {

        if (DataStore.gameMode) {
            when (DataStore.stage) {
                1 -> {
                    setContentView(R.layout.ereignisskarte)
                    initEreignisKarte()
                }
                2 -> {
                    setContentView(R.layout.quiz_online)
                    initQuiz()
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
            initRaingOnline()
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

    private fun initRaingOnline() {
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
        if (pointsFromRating == 0) {
            chosenPopout = mutableListOf(getString(R.string.no_rating),getString(R.string.no_rating_explained))
            popout()
        } else {
            DataStore.stage += 1
            if (DataStore.player1OR2) {
                DataStore.answer = hashMapOf(
                    "currentPoints1" to DataStore.currentPoints1 + pointsFromRating ,
                )
                DataStore.updateAnswerInDB()
                DataStore.currentPoints1 += pointsFromRating
            } else {
                DataStore.answer = hashMapOf(
                    "currentPoints2" to DataStore.currentPoints2 + pointsFromRating,
                )
                DataStore.updateAnswerInDB()
                DataStore.currentPoints2 += pointsFromRating
            }
            initBoard()
        }
    }

    private fun setDisplayedText(){
        if (DataStore.player1OR2){
            textViewInputVonMitspieler.text = storyText2
        }else{
            textViewInputVonMitspieler.text = storyText1
        }
        buttonWeiterBewertung.visibility = View.VISIBLE
        buttonWeiterBewertung.text = getString(R.string.bewertung_abgeben)
    }

    private fun popout(){
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


    // QUIZ


    private fun initQuiz() {
        DataStore.stage = 3
        quizWeiterOnline = findViewById(R.id.quizWeiterOnline)
        buttonAnswer1Online = findViewById(R.id.buttonAnswer1Online)
        buttonAnswer2Online = findViewById(R.id.buttonAnswer2Online)
        buttonAnswer3Online = findViewById(R.id.buttonAnswer3Online)
        buttonAnswer4Online = findViewById(R.id.buttonAnswer4Online)
        textViewFrageQuiz = findViewById(R.id.textViewFrageQuiz)
        nextQuestion()
        quizWeiterOnline.setOnClickListener {
            if (toggleButtonClicked == 0){
                chosenPopout = mutableListOf(getString(R.string.no_answer), getString(R.string.no_answer_explained))
                popout()
            }else{
                nextQuestion()
            }
        }
        buttonAnswer1Online.setOnClickListener {
            button1Clicked()
        }
        buttonAnswer2Online.setOnClickListener {
            button2Clicked()
        }
        buttonAnswer3Online.setOnClickListener {
            button3Clicked()
        }
        buttonAnswer4Online.setOnClickListener {
            button4Clicked()
        }

    }
    private fun nextQuestion() {
        toggleButtonClicked = 0
        changeToggleButtonStyle()
        //Prüft ob noch eine neue Frage angezeigt werden muss
        if (questionNumber == choosedQuestion) {
            //Aktualisiere Punkte in der DB
            DataStore.answer = hashMapOf(
                "currentPoints1" to DataStore.currentPoints1,
                "currentPoints2" to DataStore.currentPoints2

            )
            DataStore.updateAnswerInDB()
            addPoints()
            //Zurück zum Board
            initBoard()
        }else if (questionNumber != 0) {
            // Löst neuen Fragevorgang aus
            addPoints()
            selectQuestion()
            toggleButtonClicked = 0
        }else{
            // Löst ersten Fragevorgang aus
            selectQuestion()
        }

    }

    private fun addPoints() {
        //Prüft ob Antwort stimmt
        if (choosenanswer == correctAnswer ) {
            if (DataStore.player1OR2) {
                DataStore.currentPoints1 += 1
            } else {
                DataStore.currentPoints2 += 1
            }
        }
    }

    fun selectQuestion() {
        // remove current question from DS
        setNewRandomQuestion()
        setTextEtcToChosenQuestio()
        questionNumber += 1
    }



    fun setNewRandomQuestion() {
        // Wählt über eine Zuffalszahl eine Frage aus
        questionID = DataStore.questions[DataStore.questionsPicked[questionNumber]].ID
        questionText = DataStore.questions[DataStore.questionsPicked[questionNumber]].text
        textViewFrageQuiz.text = questionText
        // lässt diese Prüfen
    }

    fun setTextEtcToChosenQuestio() {
        // setzt den text der Buttons zu Fragen und Antwort

        //Speichert Fragen und Antworten in einzelnen Variabeln
        matchedAnswers = DataStore.answers.filter { it._QuestionID == questionID }
        matchAnswersText = matchedAnswers.map { it.text }
        matchAnswersId = matchedAnswers.map { it.ID }
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID }
        correctAnswer = matchedAnswers.filter { it.correct == "true" }
        questionsChosen.add(questionID)

        // Definiert Variable mit dem Text
        answer1Text = matchAnswersText[0]
        answer2Text = matchAnswersText[1]
        answer3Text = matchAnswersText[2]
        answer4Text = matchAnswersText[3]

        // Definiert Text der Buttons
        buttonAnswer1Online.textOff = answer1Text
        buttonAnswer1Online.text = answer1Text
        buttonAnswer1Online.textOn = answer1Text
        buttonAnswer2Online.textOff = answer2Text
        buttonAnswer2Online.text = answer2Text
        buttonAnswer2Online.textOn = answer2Text
        buttonAnswer3Online.textOff = answer3Text
        buttonAnswer3Online.text = answer3Text
        buttonAnswer3Online.textOn = answer3Text
        buttonAnswer4Online.textOff = answer4Text
        buttonAnswer4Online.text = answer4Text
        buttonAnswer4Online.textOn = answer4Text
    }

    private fun button1Clicked() {
        toggleButtonClicked = 1
        changeToggleButtonStyle()
        clickedAnswerID = matchAnswersId[0]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID }
    }

    private fun button2Clicked() {
        toggleButtonClicked = 2
        changeToggleButtonStyle()
        clickedAnswerID = matchAnswersId[1]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID  }
    }

    private fun button3Clicked() {
        toggleButtonClicked = 3
        changeToggleButtonStyle()
        clickedAnswerID = matchAnswersId[2]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID }
    }

    private fun button4Clicked() {
        toggleButtonClicked = 4
        changeToggleButtonStyle()
        clickedAnswerID = matchAnswersId[3]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID }
    }

    private fun changeToggleButtonStyle() {
        when (toggleButtonClicked){
            0->{
                buttonAnswer1Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer2Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer3Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer4Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
            }
            1 -> {
                buttonAnswer1Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.highlightedGrey))
                buttonAnswer2Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer3Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer4Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
            }
            2 -> {
                buttonAnswer1Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer2Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.highlightedGrey))
                buttonAnswer3Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer4Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))

            }
            3 -> {
                buttonAnswer1Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer2Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer3Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.highlightedGrey))
                buttonAnswer4Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))

            }
            4 -> {
                buttonAnswer1Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer2Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer3Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
                buttonAnswer4Online.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.highlightedGrey))

            }

        }
    }

}