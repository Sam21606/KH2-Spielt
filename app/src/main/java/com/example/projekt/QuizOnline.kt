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
    var pointsInQuiz = 0
    var questionNumber = 1
    var choosedQuestion = 4//Spieler gewählte Anzahl an Fragen
    var questionID: String = ""
    var clickedAnswerID = "0"
    lateinit var questionText: String
    lateinit var answer1Text: String
    lateinit var answer2Text: String
    lateinit var answer3Text: String
    lateinit var answer4Text: String
    private var questionsChosen = mutableListOf("")
    val matchedAnswers = DataStore.answers.filter { it._QuestionID == questionID }
    val matchAnswersText = matchedAnswers.map { it.text }
    val matchAnswersId = matchedAnswers.map { it.ID }
    var choosenanswer = matchedAnswers.filter{ clickedAnswerID == it.ID}
    var correctAnswer = matchedAnswers.filter { it.correct == "true" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_online)

        init()
    }

    fun init() {

        DataStore.stage = 3
        quizWeiterOnline = findViewById(R.id.quizWeiterOnline)
        buttonAnswer1Online = findViewById(R.id.buttonAnswer1Online)
        buttonAnswer2Online = findViewById(R.id.buttonAnswer2Online)
        buttonAnswer3Online = findViewById(R.id.buttonAnswer3Online)
        buttonAnswer4Online = findViewById(R.id.buttonAnswer4Online)
        textViewFrageQuiz = findViewById(R.id.textViewFrageQuiz)
        quizWeiterOnline.setOnClickListener {
            nextQuestion()
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

    fun oneQuestion(){buttonAnswer2Online.setOnClickListener {
        nextQuestion()
    }
        selectQuestion()
        addPoints()
    }


    private fun button1Clicked() {
        clickedAnswerID = matchAnswersId[0]
    }
    private fun button2Clicked() {
        clickedAnswerID = matchAnswersId[1]
    }
    private fun button3Clicked() {
        clickedAnswerID = matchAnswersId[2]
    }
    private fun button4Clicked() {
        clickedAnswerID = matchAnswersId[3]
    }

    fun nextQuestion() {

        if (questionNumber == choosedQuestion + 1) {
            addQuizpoints()
            val intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
        } else {
            oneQuestion()
        }

    }

    fun addQuizpoints() {
        if (DataStore.player1OR2){
            DataStore.currentPoints1 += pointsInQuiz
        }else {
            DataStore.currentPoints2 += pointsInQuiz
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
        val random: Int = (0 until (DataStore.questions.size)).random()
        questionID = DataStore.questions[random].ID
        questionText = DataStore.questions[random].text

        println("$questionID $questionText")
        textViewFrageQuiz.text = questionText
    }

    fun addPoints(){
        if (choosenanswer == correctAnswer) {
            pointsInQuiz +=1
            answerResultCorerct()
        }else {
            answerResultFalse()
        }
    }
    fun answerResultCorerct(){
        TODO()
    }

    fun answerResultFalse(){
        TODO()
    }

}