package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class QuizOffline : AppCompatActivity() {
    lateinit var quizWeiterOffline: Button
    private lateinit var buttonAnswer1Offline: ToggleButton
    private lateinit var buttonAnswer2Offline: ToggleButton
    private lateinit var buttonAnswer3Offline: ToggleButton
    private lateinit var buttonAnswer4Offline: ToggleButton
    private lateinit var answer1TextOffline: String
    private lateinit var answer2TextOffline: String
    private lateinit var answer3TextOffline: String
    private lateinit var answer4TextOffline: String
    private var questionID: String = ""
    private lateinit var questionText: String
    private lateinit var textViewFrageQuizOffline: TextView
    private var questionsChosen = mutableListOf<String>("")

    var answered1 = false
    var answered2 = false
    var answered3 = false
    var answered4 = false
    var questionNumber = 1
    var choosedQuestion = 5//Spieler gewählte Anzahl an Fragen


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_offline)

        init()
    }

    private fun init() {
        DataStore.stage = 3
        quizWeiterOffline = findViewById(R.id.quizWeiterOffline)
        buttonAnswer1Offline = findViewById(R.id.buttonAnswer1Offline)
        buttonAnswer2Offline = findViewById(R.id.buttonAnswer2Offline)
        buttonAnswer3Offline = findViewById(R.id.buttonAnswer3Offline)
        buttonAnswer4Offline = findViewById(R.id.buttonAnswer4Offline)
        textViewFrageQuizOffline = findViewById(R.id.textViewFrageQuizOffline)
        quizWeiterOffline.setOnClickListener {
            nextQuestion()
        }
        buttonAnswer1Offline.setOnClickListener {
            answerClicked()
        }
        buttonAnswer2Offline.setOnClickListener {
            answerClicked()
        }
        buttonAnswer3Offline.setOnClickListener {
            answerClicked()
        }
        buttonAnswer4Offline.setOnClickListener {
            answerClicked()
        }
        selectQuestion()
    }

    private fun answerClicked() {
        // sezte alle anderen buttons auf off
    }

    fun checkAnswer() {
        //überprüft welche antwort
    }

    fun nextQuestion() {
        selectQuestion()
        if (questionNumber == choosedQuestion + 1) {
            val intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
        }
    }

    fun selectQuestion() {
        if (questionsChosen.contains(questionID)) {
            chooseNewRandomQuestion()
            selectQuestion()
            println("Ich funktioniere")
            setTextEtcToChosenQuestio()
        } else if (!questionsChosen.contains(questionID)) {
            setTextEtcToChosenQuestio()
            questionNumber += 1
        }

    }

    fun setTextEtcToChosenQuestio() {


        val matchedAnswers = DataStore.answers.filter { it._QuestionID == questionID }
        val matchAnswersText = matchedAnswers.map { it.text }
        questionsChosen.add(questionID)

        answer1TextOffline = matchAnswersText[0]
        answer2TextOffline = matchAnswersText[1]
        answer3TextOffline = matchAnswersText[2]
        answer4TextOffline = matchAnswersText[3]

        buttonAnswer1Offline.textOff = answer1TextOffline
        buttonAnswer1Offline.text = answer1TextOffline
        buttonAnswer1Offline.textOn = answer1TextOffline
        buttonAnswer2Offline.textOff = answer2TextOffline
        buttonAnswer2Offline.text = answer2TextOffline
        buttonAnswer2Offline.textOn = answer2TextOffline
        buttonAnswer3Offline.textOff = answer3TextOffline
        buttonAnswer3Offline.text = answer3TextOffline
        buttonAnswer3Offline.textOn = answer3TextOffline
        buttonAnswer4Offline.textOff = answer4TextOffline
        buttonAnswer4Offline.text = answer4TextOffline
        buttonAnswer4Offline.textOn = answer4TextOffline
    }

    fun chooseNewRandomQuestion() {
        var random: Int = (0 until (DataStore.questions.size)).random()
        questionID = DataStore.questions[random].ID
        questionText = DataStore.questions[random].text

        println("$questionID $questionText")
        textViewFrageQuizOffline.text = questionText
    }
}