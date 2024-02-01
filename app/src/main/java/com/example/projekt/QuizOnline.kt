package com.example.projekt

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt.DataStore.db


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
    var matchedAnswers = DataStore.answers.filter { it._QuestionID == questionID }
    var matchAnswersText = matchedAnswers.map { it.text }
    var matchAnswersId = matchedAnswers.map { it.ID }
    var choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID }
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
        createGame()
        quizWeiterOnline.setOnClickListener {
            oneQuestion()
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

    fun oneQuestion() {
        nextQuestion()
        selectQuestion()
    }


    private fun button1Clicked() {
        //clickedAnswerID = matchAnswersId[0]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID }
    }

    private fun button2Clicked() {
        clickedAnswerID = matchAnswersId[1]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID }
    }

    private fun button3Clicked() {
        clickedAnswerID = matchAnswersId[2]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID }
    }

    private fun button4Clicked() {
        clickedAnswerID = matchAnswersId[3]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID }
    }

    fun nextQuestion() {

        if (questionNumber == choosedQuestion + 1) {
            addQuizpoints()
            val intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
        } else {
            addPoints()
        }

    }

    fun addQuizpoints() {
        if (DataStore.player1OR2) {
            DataStore.currentPoints1 += pointsInQuiz
        } else {
            DataStore.currentPoints2 += pointsInQuiz
        }
    }

    fun selectQuestion() {
        if (questionsChosen.contains(questionID)) {
            chooseNewRandomQuestion()
            setTextEtcToChosenQuestio()
            questionNumber += 1
        } else if (!questionsChosen.contains(questionID)) {
            setTextEtcToChosenQuestio()
            questionNumber += 1
        }

    }

    fun setTextEtcToChosenQuestio() {
        matchedAnswers = DataStore.answers.filter { it._QuestionID == questionID }
        matchAnswersText = matchedAnswers.map { it.text }
        matchAnswersId = matchedAnswers.map { it.ID }
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID }
        correctAnswer = matchedAnswers.filter { it.correct == "true" }
        questionsChosen.add(questionID)

        answer1Text = matchAnswersText[0]
        answer2Text = matchAnswersText[1]
        answer3Text = matchAnswersText[2]
        answer4Text = matchAnswersText[3]
        println("answer1Text= $answer1Text")

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
        println("Das ist die Länger ${DataStore.questions.size}")
        println("Hier sind die Answers $DataStore.logQuestionAnswers")
        println("Das ist die Länge ${DataStore.questions.size}")
        DataStore.logQuestionAnswers()
        while (DataStore.questions.size == 0){
            println("Das ist die Länger ${DataStore.questions.size}")
            println("Hier sind die Answers ${DataStore.questions}")
            println("Das ist die Länge ${DataStore.questions.size}")
        }
        if (DataStore.questions.size == 0){
            chooseNewRandomQuestion()
        }else {
            val random: Int = (0 until (DataStore.questions.size)).random()
            questionID = DataStore.questions[random].ID
            questionText = DataStore.questions[random].text
            println("questionID $questionID $questionText")
            textViewFrageQuiz.text = questionText
            }
    }


    fun addPoints() {
        if (choosenanswer == correctAnswer) {
            pointsInQuiz += 1
            answerResultCorerct()
        } else {
            answerResultFalse()
        }
    }

    fun answerResultCorerct() {
    }

    fun answerResultFalse() {
    }

    private fun createGame() {
        val game: MutableMap<String, Any> = hashMapOf(
            "GameID" to "6qUtLqiPRIP6guU8ONJw",
            "currentpoints1" to "3",
            "currentpoints2" to "3",
            "correct" to "false",
            "playerName1" to "playerName1",
            "playerName2" to "playerName1",
            "questionsPicked" to "",
            "storyText1" to "storyText1",
            "storyText2" to "storyText2",
            "topic" to "oper"
        )
        db.collection("Games")
            .add(game)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    ContentValues.TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id)
            }
        setGame()
    }

    fun setGame() {
        DataStore.currentPoints1 = 3
        DataStore.currentPoints2 = 3
        DataStore.questionsPicked = mutableListOf("")
        DataStore.topic = "oper"
        oneQuestion()

    }
}
