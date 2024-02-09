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
    private lateinit var buttonAnswer1Online: ToggleButton // inizieert die einzelnen Buttons sodass sie im code aufrufbar sind aber noch keinen Value besitzten
    private lateinit var buttonAnswer2Online: ToggleButton
    private lateinit var buttonAnswer3Online: ToggleButton
    private lateinit var buttonAnswer4Online: ToggleButton
    private lateinit var textViewFrageQuiz: TextView
    var pointsInQuiz = 0
    var questionNumber = 0
    var choosedQuestion = 3//Spieler gewählte Anzahl an Fragen
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
        nextQuestion()
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
    fun nextQuestion() {
        //Prüft ob noch eine neue Frage angezeigt werden muss

        if (questionNumber == choosedQuestion) {
            //Startet nächsten bildschirm
            addQuizpoints()
            val intent = Intent(this, Spielbrett::class.java)
            startActivity(intent)
        } else if (questionNumber != 0) {
            // Löst neuen Fragevorgang aus
            addPoints()
            selectQuestion()
        }else{
            // Löst ersten Fragevorgang aus
            selectQuestion()
        }

    }

    fun addQuizpoints() {
        //Addiert Punkte zum Richtigem Spieler im DataStore
        if (DataStore.player1OR2) {
            DataStore.currentPoints1 += pointsInQuiz
        } else {
            DataStore.currentPoints2 += pointsInQuiz
        }
    }

    fun addPoints() {
        //Prüft ob Antwort stimmt
        if (choosenanswer == correctAnswer ) {
            pointsInQuiz += 1
            answerResultCorerct()
        } else {
            answerResultFalse()
        }
    }

    fun answerResultCorerct() {
        println("Lösung korrekt die Punkte sind $pointsInQuiz")
    }

    fun answerResultFalse() {
        println("Lösung Falsch die Punkte sind $pointsInQuiz")
    }

    fun selectQuestion() {
        // Prüft on Frage Schon vorhanden war


        if (questionsChosen.contains(questionID)) {
            // Wenn ja lässt neue Auswählen
            chooseNewRandomQuestion()
        } else if (!questionsChosen.contains(questionID)) {
            // Wenn Nein lässt diese Anzeigen
            setTextEtcToChosenQuestio()
            questionNumber += 1
        }

    }
    fun chooseNewRandomQuestion() {
        // Wählt über eine Zuffalszahl eine Frage aus
        val random: Int = (0 until (DataStore.questions.size)).random()
        questionID = DataStore.questions[random].ID
        questionText = DataStore.questions[random].text
        textViewFrageQuiz.text = questionText
        selectQuestion()
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
        // Setzt die Viarablen die die Antwort auslesen auf die jeweilige Antwort fest nach jedem click
        clickedAnswerID = matchAnswersId[0]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID }
        println ("teeeeeeeeest $choosenanswer")
    }

    private fun button2Clicked() {
        // Setzt die Viarablen die die Antwort auslesen auf die jeweilige Antwort fest nach jedem click
        clickedAnswerID = matchAnswersId[1]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID  }
        println ("teeeeeeeeest $choosenanswer")
    }

    private fun button3Clicked() {
        // Setzt die Viarablen die die Antwort auslesen auf die jeweilige Antwort fest nach jedem click
        clickedAnswerID = matchAnswersId[2]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID }
        println ("teeeeeeeeest $choosenanswer $clickedAnswerID $matchedAnswers")
    }

    private fun button4Clicked() {
        // Setzt die Viarablen die die Antwort auslesen auf die jeweilige Antwort fest nach jedem click
        clickedAnswerID = matchAnswersId[3]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID }
        println ("teeeeeeeeest $choosenanswer")
    }
}
