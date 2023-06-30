package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class QuizOnline : AppCompatActivity() {
    lateinit var quizWeiterOnline: Button
    private lateinit var buttonAnswer1Online: ToggleButton
    private lateinit var buttonAnswer2Online: ToggleButton
    private lateinit var buttonAnswer3Online: ToggleButton
    private lateinit var buttonAnswer4Online: ToggleButton
    private lateinit var textViewFrageQuiz: TextView
    var answered1 = false
    var answered2 = false
    var answered3 = false
    var answered4 = false
    var questionNumber = 1
    var choosedQuestion = 4//Spieler gewählte Anzahl an Fragen
    var questionID: String = ""
    lateinit var questionText: String
    lateinit var answer1Text: String
    lateinit var answer2Text: String
    lateinit var answer3Text: String
    lateinit var answer4Text: String
    private var questionsChosen = mutableListOf<String>("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_online)

        init()
    }

    private fun init() {

        DataStore.stage = 3
        quizWeiterOnline = findViewById<Button>(R.id.quizWeiterOnline)
        buttonAnswer1Online = findViewById<ToggleButton>(R.id.buttonAnswer1Online)
        buttonAnswer2Online = findViewById<ToggleButton>(R.id.buttonAnswer2Online)
        buttonAnswer3Online = findViewById<ToggleButton>(R.id.buttonAnswer3Online)
        buttonAnswer4Online = findViewById<ToggleButton>(R.id.buttonAnswer4Online)
        textViewFrageQuiz = findViewById<TextView>(R.id.textViewFrageQuiz)
        selectQuestion()
        quizWeiterOnline.setOnClickListener {
            nextQuestion()
        }
        buttonAnswer1Online.setOnClickListener {
            answerClicked()
        }
        buttonAnswer2Online.setOnClickListener {
            answerClicked()
        }
        buttonAnswer3Online.setOnClickListener {
            answerClicked()
        }
        buttonAnswer4Online.setOnClickListener {
            answerClicked()
        }

    }

    private fun answerClicked() {
        // sezte alle anderen buttons auf off
    }

    fun checkAnswer() {
        //überprüft welche antwort
    }

    fun nextQuestion() {

        if (questionNumber == choosedQuestion + 1) {
            val intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
        } else {
            selectQuestion()
        }

    }

    fun selectQuestion() {
        if (questionsChosen.contains(questionID)) {
            chooseNewRandomQuestion()
            selectQuestion()
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

        answer1Text = matchAnswersText[0]
        answer2Text = matchAnswersText[1]
        answer3Text = matchAnswersText[2]
        answer4Text = matchAnswersText[3]

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

    fun chooseNewRandomQuestion() {
        var random: Int = (0 until (DataStore.questions.size)).random()
        questionID = DataStore.questions[random].ID
        questionText = DataStore.questions[random].text

        println("$questionID $questionText")
        textViewFrageQuiz.text = questionText
    }
}