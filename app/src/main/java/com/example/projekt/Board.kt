package com.example.projekt

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class Board : AppCompatActivity(){
    // Variables related to the board
    private lateinit var textViewStage: TextView
    private lateinit var textViewPlayer1: TextView
    private lateinit var textViewPlayer2: TextView
    private lateinit var imagePlayer1: ImageView
    private lateinit var imagePlayer2: ImageView
    private lateinit var buttonSpielbrett: Button

    private var storyText1 = "Warte auf Eingabe"
    private var storyText2 = "Warte auf Eingabe"
    private var playerCanContinue = false
    private var player1IsReady = false
    private var player2IsReady = false
    private var currentPoints1Before = 0
    private var currentPoints2Before = 0
    private var thereIsTheStoryInput = false
    private var ratingIsInitalised = false
    private var ediTextInput = ""

    // Variables related to Event Card
    private lateinit var buttonWeiterEreigniskarte: Button

    // Variables related to Answer Input
    private lateinit var weiterButton: Button
    private lateinit var ediTextStoryInput: TextInputEditText

    // Variables related to rating
    private lateinit var ratingBewertung: RatingBar
    private lateinit var buttonWeiterBewertung: Button
    private lateinit var textViewInputVonMitspieler: TextView

    // Variables related to quiz
    private lateinit var quizWeiterOnline: Button
    private lateinit var buttonAnswer1Online: ToggleButton
    private lateinit var buttonAnswer2Online: ToggleButton
    private lateinit var buttonAnswer3Online: ToggleButton
    private lateinit var buttonAnswer4Online: ToggleButton
    private lateinit var textViewFrageQuiz: TextView
    private var questionNumber = 0
    private var choosedQuestion = DataStore.questionsPicked.size
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
    private val buttonTintMap = mapOf(
        1 to R.color.highlightedGrey,
        2 to R.color.grey,
        3 to R.color.grey,
        4 to R.color.grey
    )

    // Culture Experinece
    private lateinit var cultureContinue: Button
    private var cultureText1 = ""
    private var cultureText2 = ""

    // Firebase
    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBoard()
        setSnapshotListener()
    }

    private fun initBoard() {
        setContentView(R.layout.board)
        playerCanContinue = false // muss warten bis anderer Spieler bereit ist
        buttonSpielbrett = findViewById(R.id.buttonSpielbrett)
        textViewStage = findViewById(R.id.textViewBoard)
        textViewPlayer1 = findViewById(R.id.textViewPlayer1)
        textViewPlayer2 = findViewById(R.id.textViewPlayer2)
        imagePlayer1 = findViewById(R.id.imagePlayer1)
        imagePlayer2 = findViewById(R.id.imagePlayer2)
        imagePlayer1.setImageResource(DataStore.images[DataStore.choosenAvatar1])
        imagePlayer2.setImageResource(DataStore.images[DataStore.choosenAvatar2])
        textViewStage.text = getString(R.string.Stage_text , DataStore.stage.toString())
        setPointsViews()
        buttonSpielbrett.setOnClickListener {
            setNewActivity()
        }
        updatePlayerStatusInDB()
    }

    private fun updatePlayerStatusInDB() {
        if (DataStore.player1OR2){
            DataStore.answer = hashMapOf(
                "player1IsReady" to true,
                "stage" to DataStore.stage
            )
            DataStore.updateAnswerInDB()
            player1IsReady = true
        }else{
            DataStore.answer = hashMapOf(
                "player2IsReady" to true,
                "stage" to DataStore.stage
            )
            DataStore.updateAnswerInDB()
            player2IsReady = true
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
                cultureText1 = snapshot.get("cultureText1").toString()
                cultureText2 = snapshot.get("cultureText2").toString()
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
        if (currentPoints2Before != DataStore.currentPoints2 || currentPoints1Before != DataStore.currentPoints1) {
            setPointsViews()
        }
        if (storyText2 != "Warte auf Eingabe" && DataStore.player1OR2 && ratingIsInitalised && DataStore.stage == 1){
            thereIsTheStoryInput = true
            setDisplayedText()
        }else if (!DataStore.player1OR2 && storyText1 != "Warte auf Eingabe" && ratingIsInitalised&& DataStore.stage == 1){
            thereIsTheStoryInput = true
            setDisplayedText()
        }else if (storyText2 != "Warte auf Eingabe" && DataStore.player1OR2&& DataStore.stage == 1){
            thereIsTheStoryInput = true
        }else if (storyText1 != "Warte auf Eingabe" && !DataStore.player1OR2&& DataStore.stage == 1){
            thereIsTheStoryInput = true
        }
        if (cultureText2 != "Warte auf Eingabe" && DataStore.player1OR2 && ratingIsInitalised&& DataStore.stage == 3 ){
            thereIsTheStoryInput = true
            setDisplayedText()
        }else if (!DataStore.player1OR2 && cultureText1 != "Warte auf Eingabe" && ratingIsInitalised&& DataStore.stage == 3){
            thereIsTheStoryInput = true
            setDisplayedText()
        }else if (cultureText2 != "Warte auf Eingabe" && DataStore.player1OR2&& DataStore.stage == 3){
            thereIsTheStoryInput = true
        }else if (cultureText2 != "Warte auf Eingabe" && !DataStore.player1OR2&& DataStore.stage == 3){
            thereIsTheStoryInput = true
        }
        if (player1IsReady && player2IsReady){
            playerCanContinue = true
        }
    }

    private fun setPointsViews() {
        textViewPlayer1.text = getString(
            R.string.player_points,
            DataStore.playerName1,
            DataStore.currentPoints1.toString()
        )
        textViewPlayer2.text = getString(
            R.string.player_points,
            DataStore.playerName2,
            DataStore.currentPoints2.toString()
        )
    }

    private fun setNewActivity() {
        if (playerCanContinue) {
            when (DataStore.stage) {
                1 -> {
                    setContentView(R.layout.ereignisskarte)
                    initEventCard()
                }

                2 -> {
                    setContentView(R.layout.quiz)
                    initQuiz()
                }

                3 -> {
                    setContentView(R.layout.culuture_experinece)
                    initCultureExperinece()
                }

                4 -> {
                    setContentView(R.layout.win_screen)
                }
            }
            updatePlayerStatusInDBLogOf()

        }
    }



    private fun updatePlayerStatusInDBLogOf(){
        if (DataStore.player1OR2 && player1IsReady){
            DataStore.answer = hashMapOf(
                "player1IsReady" to false,
                "player2IsReady" to false,
            )
            DataStore.updateAnswerInDB()
        }else if (!DataStore.player1OR2 && player2IsReady){
            DataStore.answer = hashMapOf(
                "player1IsReady" to false,
                "player2IsReady" to false,
            )
            DataStore.updateAnswerInDB()
        }
    }


    // Event Card
    private fun initEventCard() {
        buttonWeiterEreigniskarte = findViewById(R.id.buttonWeiterEreigniskarte)
        buttonWeiterEreigniskarte.setOnClickListener {
            initAnswerInput()
        }
    }


    // Answer Input
    private fun initAnswerInput() {
        setContentView(R.layout.answer_input)
        weiterButton = findViewById(R.id.Weiter)
        ediTextStoryInput = findViewById(R.id.ediTextStoryInput)

        weiterButton.setOnClickListener {
            ediTextInput = ediTextStoryInput.text.toString()
            setStoryText()
            setContentView(R.layout.rating_online)
            initRating()
        }
    }

    private fun setStoryText() {
        if (DataStore.stage == 1) {
            if (DataStore.player1OR2) {
                DataStore.answer = hashMapOf(
                    "storyText1" to ediTextInput ,
                )
                DataStore.updateAnswerInDB()
            } else {
                DataStore.answer = hashMapOf(
                    "storyText2" to ediTextInput ,
                )
                DataStore.updateAnswerInDB()
            }
        }else
            if (DataStore.player1OR2) {
                DataStore.answer = hashMapOf(
                    "cultureText1" to ediTextInput ,
                )
                DataStore.updateAnswerInDB()
            } else {
                DataStore.answer = hashMapOf(
                    "cultureText2" to ediTextInput ,
                )
                DataStore.updateAnswerInDB()
            }

    }




    // Rating

    private fun initRating() {
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
            checkRating()
        }
    }

    private fun checkRating() {
        val pointsFromRating = ratingBewertung.rating.toInt()
        if (pointsFromRating == 0) {
            DataStore.chosenPopout = mutableListOf(getString(R.string.no_rating),getString(R.string.no_rating_explained))
            DataStore.popout(this)
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
            ratingIsInitalised = false
            thereIsTheStoryInput = false
            initBoard()
        }
    }

    private fun setDisplayedText(){
        if(DataStore.stage == 1) {
            if (DataStore.player1OR2) {
                textViewInputVonMitspieler.text = storyText2
            } else {
                textViewInputVonMitspieler.text = storyText1
            }
        }else{
            if (DataStore.player1OR2) {
                textViewInputVonMitspieler.text = cultureText2
            } else {
                textViewInputVonMitspieler.text = cultureText1
            }
        }
        buttonWeiterBewertung.visibility = View.VISIBLE
        buttonWeiterBewertung.text = getString(R.string.bewertung_abgeben)
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
                DataStore.chosenPopout = mutableListOf(getString(R.string.no_answer), getString(R.string.no_answer_explained))
                DataStore.popout(this)
            }else{
                nextQuestion()
            }
        }
        buttonAnswer1Online.setOnClickListener {
            buttonClicked(1)
        }
        buttonAnswer2Online.setOnClickListener {
            buttonClicked(2)
        }
        buttonAnswer3Online.setOnClickListener {
            buttonClicked(3)
        }
        buttonAnswer4Online.setOnClickListener {
            buttonClicked(4)
        }

    }
    private fun nextQuestion() {
        toggleButtonClicked = 0
        changeToggleButtonStyle()
        //Prüft ob noch eine neue Frage angezeigt werden muss
        if (questionNumber == choosedQuestion) {
            addPoints()
            //Aktualisiere Punkte in der DB
            DataStore.answer = hashMapOf(
                "currentPoints1" to DataStore.currentPoints1,
                "currentPoints2" to DataStore.currentPoints2

            )
            DataStore.updateAnswerInDB()
            //Zurück zum Board
            initBoard()
        }else if (questionNumber != 0) {
            // Löst neuen Fragevorgang aus
            addPoints()
            setNewQuestion()
            setTextEtcToChosenQuestio()
            questionNumber += 1
            toggleButtonClicked = 0
        }else{
            // Löst ersten Fragevorgang aus
            setNewQuestion()
            setTextEtcToChosenQuestio()
            questionNumber += 1
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

    private fun setNewQuestion() {
        // Wählt über eine Zuffalszahl eine Frage aus
        questionID = DataStore.questions[DataStore.questionsPicked[questionNumber]].ID
        questionText = DataStore.questions[DataStore.questionsPicked[questionNumber]].text
        textViewFrageQuiz.text = questionText
        // lässt diese Prüfen
    }

    private fun setTextEtcToChosenQuestio() {
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
        buttonAnswer1Online.text = answer1Text
        buttonAnswer2Online.text = answer2Text
        buttonAnswer3Online.text = answer3Text
        buttonAnswer4Online.text = answer4Text
        buttonAnswer1Online.textOff = answer1Text
        buttonAnswer2Online.textOff = answer2Text
        buttonAnswer3Online.textOff = answer3Text
        buttonAnswer4Online.textOff = answer4Text
        buttonAnswer1Online.textOn = answer1Text
        buttonAnswer2Online.textOn = answer2Text
        buttonAnswer3Online.textOn = answer3Text
        buttonAnswer4Online.textOn = answer4Text
    }

    private fun buttonClicked(buttonNumber: Int) {
        // Handle button click for quiz answers
        val clickedAnswerID = when (buttonNumber) {
            1 -> matchAnswersId[0]
            2 -> matchAnswersId[1]
            3 -> matchAnswersId[2]
            4 -> matchAnswersId[3]
            else -> ""
        }
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID }
        toggleButtonClicked = buttonNumber
        changeToggleButtonStyle(toggleButtonClicked)
    }

    private fun changeToggleButtonStyle(toggleButtonClicked: Int) {
        buttonTintMap.forEach { (buttonNumber, colorResId) ->
            val button = when (buttonNumber) {
                1 -> buttonAnswer1Online
                2 -> buttonAnswer2Online
                3 -> buttonAnswer3Online
                4 -> buttonAnswer4Online
                else -> null
            }
            button?.backgroundTintList = if (buttonNumber == toggleButtonClicked) {
                ColorStateList.valueOf(ContextCompat.getColor(this, colorResId))
            } else {
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
            }
        }
    }
    private fun changeToggleButtonStyle() {
        buttonTintMap.forEach { (buttonNumber, _) ->
            val button = when (buttonNumber) {
                1 -> buttonAnswer1Online
                2 -> buttonAnswer2Online
                3 -> buttonAnswer3Online
                4 -> buttonAnswer4Online
                else -> null
            }
            button?.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.grey)
            )
        }
    }

    // Culture Experinece
    private fun initCultureExperinece() {
        cultureContinue = findViewById(R.id.cultureContinue)
        cultureContinue.setOnClickListener {
            initAnswerInput()
        }
    }
}