package com.example.projekt

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class QuizOffline : AppCompatActivity() {
    private lateinit var quizWeiterOnline: Button
    private lateinit var buttonAnswer1Online: ToggleButton // inizieert die einzelnen Buttons sodass sie im code aufrufbar sind aber noch keinen Value besitzten
    private lateinit var buttonAnswer2Online: ToggleButton
    private lateinit var buttonAnswer3Online: ToggleButton
    private lateinit var buttonAnswer4Online: ToggleButton
    private lateinit var textViewFrageQuiz: TextView
    private lateinit var playerName : TextView
    private var questionNumber = 0
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
    private var chosenPopout : MutableList <String> = mutableListOf() // erste Stelle Titel zweite Stelle Erklärung


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_online)

        init()
    }

    private fun init() {
        DataStore.stage = 3
        quizWeiterOnline = findViewById(R.id.quizWeiterOnline)
        buttonAnswer1Online = findViewById(R.id.buttonAnswer1Online)
        buttonAnswer2Online = findViewById(R.id.buttonAnswer2Online)
        buttonAnswer3Online = findViewById(R.id.buttonAnswer3Online)
        buttonAnswer4Online = findViewById(R.id.buttonAnswer4Online)
        textViewFrageQuiz = findViewById(R.id.textViewFrageQuiz)
        playerName = findViewById(R.id.playerName)

        playerName.text = DataStore.playerName1
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
        //Prüft ob noch eine neue Frage angezeigt werden muss
        if (questionNumber == DataStore.questionCount * 2) {//questionCount = Anzahl an Fragen pro Spieler
            addPoints()
            DataStore.player1OR2 = true
            intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
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
        if (choosenanswer == correctAnswer) {
            if (questionNumber == DataStore.questionCount + 1){
                DataStore.player1OR2 = false
                playerName.text = DataStore.playerName2
            }
            if (DataStore.player1OR2) {
                DataStore.currentPoints1 += 1
                println("Punkte")
            } else {
                DataStore.currentPoints2 += 1
                println("Punkte2")
            }
        }else{
            println("Du bist dumm ${DataStore.player1OR2}")
        }
    }

    private fun selectQuestion() {
        // remove current question from DS
        setNewRandomQuestion()
        setTextEtcToChosenQuestio()
        questionNumber += 1
    }



    private fun setNewRandomQuestion() {
        questionID = DataStore.questions[DataStore.questionsPicked[questionNumber]].ID
        questionText = DataStore.questions[DataStore.questionsPicked[questionNumber]].text
        textViewFrageQuiz.text = questionText
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
        println ("teeeeeeeeest $choosenanswer")
    }



    private fun button2Clicked() {
        toggleButtonClicked = 2
        changeToggleButtonStyle()
        clickedAnswerID = matchAnswersId[1]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID  }
        println ("teeeeeeeeest $choosenanswer")
    }

    private fun button3Clicked() {
        toggleButtonClicked = 3
        changeToggleButtonStyle()
        clickedAnswerID = matchAnswersId[2]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID }
        println ("teeeeeeeeest $choosenanswer $clickedAnswerID $matchedAnswers")
    }

    private fun button4Clicked() {
        toggleButtonClicked = 4
        changeToggleButtonStyle()
        clickedAnswerID = matchAnswersId[3]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID }
        println ("teeeeeeeeest $choosenanswer")
    }

    private fun changeToggleButtonStyle() {
        when (toggleButtonClicked){
            1 -> {
                buttonAnswer1Online.text = "ausgewählt"
                buttonAnswer2Online.text
                buttonAnswer3Online.text
                buttonAnswer4Online.text
            }
            2 -> {
                buttonAnswer1Online
                buttonAnswer2Online.text = "ausgewählt"
                buttonAnswer3Online
                buttonAnswer4Online

            }
            3 -> {
                buttonAnswer1Online
                buttonAnswer2Online
                buttonAnswer3Online.text = "ausgewählt"
                buttonAnswer4Online

            }
            4 -> {
                buttonAnswer1Online
                buttonAnswer2Online
                buttonAnswer3Online
                buttonAnswer4Online.text = "ausgewählt"

            }

        }
    }

    private fun popout(){
        val popoutNoInput =
            layoutInflater.inflate(R.layout.popout_template, null)
        val popout = Dialog(this)
        val popoutText = popoutNoInput.findViewById<TextView>(R.id.popoutText)
        val popoutTitle = popoutNoInput.findViewById<TextView>(R.id.popoutTitle)
        popoutText.text = chosenPopout[1]
        popoutTitle.text = chosenPopout[0]
        popout.setContentView(R.layout.popout_template)
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